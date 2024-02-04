package io.architecture.playground.data

import android.util.Log
import io.architecture.playground.data.local.LocalNodesDataSource
import io.architecture.playground.data.mapping.toExternal
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.remote.NetworkNodeDataSource
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.model.Node
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DefaultNodeRepository @Inject constructor(
    private val networkDataSource: NetworkNodeDataSource,
    private val localDataSource: LocalNodesDataSource
) : NodeRepository {

    override fun getStreamConnectionState(): Flow<ConnectionState> =
        networkDataSource.observeConnection()
            .filter { it.type != SocketConnectionState.MESSAGE_RECEIVED }

    override fun getStreamNodes(): Flow<Node> = networkDataSource.streamNodes()
        .map { it.toExternal() }
        .onEach {
            localDataSource.add(it.toLocal())
        }
        .catch { error -> Log.d("REPOSITORY", "error - $error") }

    override fun getStreamCountNodes(): Flow<Long> = localDataSource.observeCountNodes()

    override fun deleteAll() = localDataSource.deleteAll()

    override fun getStreamTraceHistory(): Flow<List<Node>> = flow { }

    override fun getStreamLatestNodes(): Flow<List<Node>> =
        localDataSource.observeAll().map { it.toExternal() }

    override suspend fun getAllTracesBy(nodeId: String): List<Node> =
        localDataSource.getAllTracesByNodeId(nodeId).map { it.toExternal() }

}