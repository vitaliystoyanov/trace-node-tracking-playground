package io.architecture.data.repository

import android.util.Log
import io.architecture.data.mapping.toExternal
import io.architecture.data.mapping.toExternalAs
import io.architecture.data.mapping.toNode
import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.data.repository.interfaces.TraceRepository
import io.architecture.database.api.model.toExternal
import io.architecture.database.api.model.toLocal
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

open class DefaultTraceRepository(
    private val nodeRepository: NodeRepository,
    networkDataSource: NetworkDataSource,
    private val localDataSource: LocalDataSource,
    applicationScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : TraceRepository {

    private val _sharedStreamTraces: SharedFlow<Trace> =
        networkDataSource.streamTraces()
            .map { it.toExternal() }
            .shareIn(
                applicationScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )

    override fun streamTraceBy(nodeId: String): Flow<Trace> =
        localDataSource.observeTraceBy(nodeId)
            .map { it.toExternalAs() }

    //  NOTE: Bulk insertion of items in an SQLite table is always better than inserting each item individually
    override fun streamAndPersist(): Flow<Trace> = _sharedStreamTraces
        .buffer(1000, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .onEach { trace ->
            localDataSource.createOrUpdate(trace.toLocal())
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
            .map { it.toExternal() }
            .flowOn(defaultDispatcher)
}
