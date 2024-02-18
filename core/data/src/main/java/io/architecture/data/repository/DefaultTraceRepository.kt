package io.architecture.data.repository

import android.util.Log
import io.architecture.common.ApplicationScope
import io.architecture.common.DefaultDispatcher
import io.architecture.common.IoDispatcher
import io.architecture.data.mapping.toExternalAs
import io.architecture.data.mapping.toLocal
import io.architecture.data.mapping.toNode
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.datasource.api.LocalDataSource
import io.architecture.datasource.api.NetworkDataSource
import io.architecture.model.Trace
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
) : TraceRepository {

    private val _sharedStreamTraces: SharedFlow<Trace> =
        networkDataSource.streamTraces()
            .map { it.toExternalAs() }
            .shareIn(
                applicationScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )

    override fun streamTraceBy(nodeId: String): Flow<Trace> =
        localDataSource.observeTraceBy(nodeId)
            .map { it.toExternalAs() }

    // TODO Bulk insertion of items in an SQLite table is always better than inserting each item individually
    // TODO Extract _sharedStreamTraces logic
    override fun streamAndPersist(): Flow<Trace> = _sharedStreamTraces
        .buffer(1000, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .onEach { trace ->
            localDataSource.createOrUpdate(trace.toLocal()) // TODO Move to TraceRepository
            nodeRepository.createOrUpdate(trace.toNode())
        }
        .catch { error -> Log.d("REPOSITORY_DEBUG", "error - $error") }

    override fun streamViaNetwork(): Flow<Trace> = _sharedStreamTraces
        .buffer(1000, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .catch { error -> Log.d("REPOSITORY_DEBUG", "error - $error") }

    override fun streamCount(): Flow<Int> = localDataSource.observeTraceCount()

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        localDataSource.deleteAllTraces()
    }

    override fun streamTracesBy(nodeId: String): Flow<Trace> =
        localDataSource.observeTraceBy(nodeId)
            .map { it.toExternalAs() }

    override fun streamList(): Flow<List<Trace>> =
        localDataSource.observeAllTraces()
            .map { it.toExternalAs() }
            .flowOn(defaultDispatcher)
}
