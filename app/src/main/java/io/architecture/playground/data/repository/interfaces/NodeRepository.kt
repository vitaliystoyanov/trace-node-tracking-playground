package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.model.Node
import io.architecture.playground.model.Route
import kotlinx.coroutines.flow.Flow

interface NodeRepository {
    fun observeConnectionState(): Flow<ConnectionState>

    fun observeAndStoreNodes(): Flow<Node>

    fun observeNodesCount(): Flow<Int>

    suspend fun deleteAll()

    fun observeLatestNodesWithRoute(): Flow<List<Pair<Node, Route>>>

    fun observeListNodes(): Flow<List<Node>>
}