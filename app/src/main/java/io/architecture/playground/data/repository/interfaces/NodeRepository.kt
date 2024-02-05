package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.model.Node
import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.Flow

interface NodeRepository {
    fun observeConnectionState(): Flow<ConnectionState>

    fun observeAndStoreNodes(): Flow<Node>

    fun observeNodesCount(): Flow<Long>

    suspend fun deleteAll()

    fun observeLatestNodes(): Flow<List<Node>>

    fun observeLatestNodesWithRoute(): Flow<List<Pair<Node, Route>>>

}