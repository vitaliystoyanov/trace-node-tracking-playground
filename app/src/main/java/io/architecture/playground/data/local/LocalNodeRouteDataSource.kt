package io.architecture.playground.data.local

import io.architecture.playground.data.local.model.LocalNode
import io.architecture.playground.data.local.model.LocalNodeWithRoute
import io.architecture.playground.data.local.model.LocalRoute
import kotlinx.coroutines.flow.Flow

interface LocalNodeRouteDataSource {

    suspend fun getRouteBy(nodeId: String): LocalRoute?

    suspend fun add(node: LocalNode)

    suspend fun add(route: LocalRoute)

    fun observeAllNodes(): Flow<List<LocalNode>>

    fun observeAllNodesWithRoute(): Flow<List<LocalNodeWithRoute>>

    fun observeCountNodes(): Flow<Long>

    suspend fun getAllNodes(): List<LocalNode>

    suspend fun deleteAllNodes()

    suspend fun deleteAllNodesWithRoute()

    fun observeLatestNode(): Flow<LocalNode>

    suspend fun getAllTracesByNodeId(nodeId: String): List<LocalNode>

}