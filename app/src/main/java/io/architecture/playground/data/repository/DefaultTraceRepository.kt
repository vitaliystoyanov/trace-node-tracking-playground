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
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Node
import io.architecture.playground.model.NodeMode
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultTraceRepository @Inject constructor(
    private val nodeRepository: NodeRepository,
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    poolHolder: PoolManager
) : DefaultStreamConnectionRepository(networkDataSource), TraceRepository {

    private val nodePool = poolHolder.getPoolByMember<Node>()
    private val traceEntitiesPool = poolHolder.getPoolByMember<TraceEntity>()

    override fun observeAndStore(): Flow<Trace> = networkDataSource.streamTraces()
        .onEach { trace ->
            val localTraceEntity = traceEntitiesPool.acquire()!!
            val nodePooled = nodePool.acquire()!!.apply {// TODO Move to external mapping
                id = trace.nodeId
                mode = NodeMode.valueOf(trace.mode)
                lastTraceTimestamp = trace.time
            }

            localDataSource.updateOrCreate(trace.assignProperties(localTraceEntity, trace))
            nodeRepository.updateOrAdd(nodePooled)

            traceEntitiesPool.release(localTraceEntity)
            nodePool.release(nodePooled)
        }
        .flowOn(ioDispatcher)
        .map { it.toExternal() }
        .flowOn(defaultDispatcher)
        .catch { error -> Log.d("REPOSITORY", "error - $error") }

    override fun observeCount(): Flow<Int> = localDataSource.observeTraceCount()

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        localDataSource.deleteAllTraces()
    }

    override fun observeList(): Flow<List<Trace>> =
        localDataSource.observeAllTraces()
            .map { it.toExternal() }
            .flowOn(defaultDispatcher)
}