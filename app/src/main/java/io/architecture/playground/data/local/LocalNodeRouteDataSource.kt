package io.architecture.playground.data.local

import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.NodeWithRouteEntity
import io.architecture.playground.data.local.model.RouteEntity
import kotlinx.coroutines.flow.Flow

interface LocalNodeRouteDataSource {

    fun observeAllNodes(): Flow<List<NodeEntity>>

    fun observeAllNodesWithRoute(): Flow<List<NodeWithRouteEntity>>

    fun observeCountNodes(): Flow<Int>

    suspend fun getRouteBy(nodeId: String): RouteEntity?

    suspend fun add(node: NodeEntity)

    suspend fun add(route: RouteEntity)

    suspend fun getAllNodes(): List<NodeEntity>

    suspend fun deleteAllNodes()

    suspend fun deleteAllNodesWithRoute()


}