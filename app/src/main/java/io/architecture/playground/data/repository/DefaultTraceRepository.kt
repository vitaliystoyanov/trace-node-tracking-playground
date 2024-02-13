package io.architecture.playground.data.repository

import android.util.Log
import io.architecture.playground.core.pool.PoolManager
import io.architecture.playground.data.local.LocalDataSource
import io.architecture.playground.data.local.model.TraceEntity
import io.architecture.playground.data.mapping.assignProperties
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.TraceRepository
import io.architecture.playground.di.ApplicationScope
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Node
import io.architecture.playground.model.NodeMode
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class DefaultTraceRepository @Inject constructor(
    private val nodeRepository: NodeRepository,
    networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    poolHolder: PoolManager
) : TraceRepository {

    private val nodePool = poolHolder.getPoolByMember<Node>()
    private val traceEntitiesPool = poolHolder.getPoolByMember<TraceEntity>()
    override val sharedStreamTraces: SharedFlow<Trace> =
        networkDataSource.streamTraces()  // TODO extract to network data source
            .shareIn(
                applicationScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )

    override fun streamTraceBy(nodeId: String): Flow<Trace> = localDataSource.observeTraceBy(nodeId)
        .map { it.toExternal() }


    // TODO Bulk insertion of items in an SQLite table is always better than inserting each item individually
    // TODO Extract _sharedStreamTraces logic
    override fun streamAndPersist(): Flow<Trace> = sharedStreamTraces
//        .flowOn(ioDispatcher)
        .buffer(1000, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .onEach { trace ->
            val localTraceEntity = traceEntitiesPool.acquire()
            val nodePooled = nodePool.acquire().apply {// TODO Move to external mapping
                id = trace.nodeId
                mode = NodeMode.ACTIVE // TODO
            }

            localDataSource.createOrUpdate(trace.assignProperties(localTraceEntity, trace))
            nodeRepository.createOrUpdate(nodePooled)

            traceEntitiesPool.release(localTraceEntity)
            nodePool.release(nodePooled)
        }
        .catch { error -> Log.d("REPOSITORY_DEBUG", "error - $error") }

    override fun streamViaNetwork(): Flow<Trace> = sharedStreamTraces
        .buffer(1000, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .catch { error -> Log.d("REPOSITORY_DEBUG", "error - $error") }

    override fun streamCount(): Flow<Int> = localDataSource.observeTraceCount()

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        localDataSource.deleteAllTraces()
    }

    override fun streamTracesBy(nodeId: String) = localDataSource.observeTraceBy(nodeId)

    override fun streamList(): Flow<List<Trace>> =
        localDataSource.observeAllTraces()
            .map { it.toExternal() }
            .flowOn(defaultDispatcher)
}