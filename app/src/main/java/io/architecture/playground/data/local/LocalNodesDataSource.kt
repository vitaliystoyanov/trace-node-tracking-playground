package io.architecture.playground.data.local

import kotlinx.coroutines.flow.Flow

interface LocalNodesDataSource {

    suspend fun add(node: LocalNode)

    fun observeAll(): Flow<List<LocalNode>>

    fun observeCountNodes(): Flow<Long>

    suspend fun getAll(): List<LocalNode>

    fun deleteAll()

    fun observeNodeLatest(): Flow<LocalNode>

    suspend fun getAllTracesByNodeId(nodeId: String) : List<LocalNode>

}