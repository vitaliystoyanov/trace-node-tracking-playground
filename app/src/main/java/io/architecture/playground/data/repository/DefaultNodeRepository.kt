package io.architecture.playground.data.repository

import android.util.Log
import io.architecture.playground.data.local.LocalNodeRouteDataSource
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Node
import io.architecture.playground.model.Route
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DefaultNodeRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalNodeRouteDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NodeRepository {

    override fun observeConnectionState(): Flow<ConnectionState> =
        networkDataSource.observeConnection()
            .filter { it.type != SocketConnectionState.MESSAGE_RECEIVED }
            .onEach { state ->
                if (state.type in arrayOf(
                        SocketConnectionState.FAILED,
                        SocketConnectionState.CLOSED,
                        SocketConnectionState.CLOSING
                    )
                ) {
                    localDataSource.deleteAllNodes()
                }
            }

    override fun observeAndStoreNodes(): Flow<Node> = networkDataSource.streamNodes()
        .onEach {
            localDataSource.add(it.toLocal())
        }
        .flowOn(ioDispatcher)
        .map { it.toExternal() }
        .flowOn(defaultDispatcher)
        .catch { error -> Log.d("REPOSITORY", "error - $error") }

    override fun observeNodesCount(): Flow<Int> = localDataSource.observeCountNodes()

    override suspend fun deleteAll() = localDataSource.deleteAllNodes()

    override fun observeListNodes(): Flow<List<Node>> =
        localDataSource.observeAllNodes()
            .map { it.toExternal() }
            .flowOn(defaultDispatcher)

    override fun observeLatestNodesWithRoute(): Flow<List<Pair<Node, Route>>> =
        localDataSource.observeAllNodesWithRoute()
            .map { list ->
                list.map { nodeWithRoute ->
                    Pair(nodeWithRoute.node.toExternal(), nodeWithRoute.route.toExternal())
                }
            }
}