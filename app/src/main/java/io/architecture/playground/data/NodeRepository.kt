package io.architecture.playground.data

import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.model.Node
import kotlinx.coroutines.flow.Flow

interface NodeRepository {

    fun getStreamConnectionState(): Flow<ConnectionState>

    fun getStreamNodes(): Flow<Node>

    fun getStreamCountNodes(): Flow<Long>

    fun deleteAll()

    fun getStreamTraceHistory(): Flow<List<Node>>

    fun getStreamLatestNodes(): Flow<List<Node>>

    suspend fun getAllTracesBy(nodeId: String): List<Node>

}