package io.architecture.playground.data.repository

import android.util.Log
import io.architecture.playground.data.local.LocalDataSource
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.data.repository.interfaces.TraceRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TraceRepository {

    override fun observeConnectionState(): Flow<ConnectionState> =
        networkDataSource.streamConnection()
            .filter { it.type != SocketConnectionState.MESSAGE_RECEIVED }
            .onEach { state ->
                if (state.type in arrayOf(
                        SocketConnectionState.FAILED,
                        SocketConnectionState.CLOSED,
                        SocketConnectionState.CLOSING
                    )
                ) {
                    localDataSource.deleteAllTraces()
                }
            }

    override fun observeAndStore(): Flow<Trace> = networkDataSource.streamTraces()
        .onEach { trace ->
            localDataSource.updateOrCreate(trace.toLocal())
            nodeRepository.updateOrAdd(Node(trace.nodeId, trace.mode, trace.time))
        }
        .flowOn(ioDispatcher)
        .map { it.toExternal() }
        .flowOn(defaultDispatcher)
        .catch { error -> Log.d("REPOSITORY", "error - $error") }

    override fun observeCount(): Flow<Int> = localDataSource.observeNodeCount()

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        localDataSource.deleteAllTraces()
    }

    override fun observeList(): Flow<List<Trace>> =
        localDataSource.observeAllTraces()
            .map { it.toExternal() }
            .flowOn(defaultDispatcher)
}