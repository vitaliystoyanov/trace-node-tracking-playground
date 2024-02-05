package io.architecture.playground.data.repository

import android.util.Log
import io.architecture.playground.data.local.LocalNodeRouteDataSource
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.interfaces.NetworkDataSource
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.model.Node
import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DefaultNodeRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val localDataSource: LocalNodeRouteDataSource
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
        .map { it.toExternal() }
        .onEach {
            localDataSource.add(it.toLocal())
        }
        .catch { error -> Log.d("REPOSITORY", "error - $error") }

    override fun observeNodesCount(): Flow<Long> = localDataSource.observeCountNodes()

    override suspend fun deleteAll() = localDataSource.deleteAllNodes()

    override fun observeLatestNodes(): Flow<List<Node>> =
        localDataSource.observeAllNodes().map { it.toExternal() }

    override fun observeLatestNodesWithRoute(): Flow<List<Pair<Node, Route>>> =
        localDataSource.observeAllNodesWithRoute()
            .map { list ->
                list.map { nodeWithRoute ->
                    Pair(nodeWithRoute.node.toExternal(), nodeWithRoute.route.toExternal())
                }
            }
}