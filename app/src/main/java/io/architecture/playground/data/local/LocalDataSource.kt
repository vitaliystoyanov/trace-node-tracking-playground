package io.architecture.playground.data.local

import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.NodeWithLastTraceEntity
import io.architecture.playground.data.local.model.RouteEntity
import io.architecture.playground.data.local.model.TraceEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun observeAllTraces(): Flow<List<TraceEntity>>

    fun observeAllNodes(): Flow<List<NodeEntity>>

    fun observeAllNodesWithLatestTrace(): Flow<List<NodeWithLastTraceEntity>>

    fun observeNodeCount(): Flow<Int>

    suspend fun getAllNodesWithLatestTrace(): List<NodeWithLastTraceEntity>

    suspend fun getRouteBy(nodeId: String): RouteEntity?

    suspend fun updateOrCreate(node: NodeEntity)

    suspend fun updateOrCreate(trace: TraceEntity)

    suspend fun updateOrCreate(route: RouteEntity)

    suspend fun getAllTraces(): List<TraceEntity>

    suspend fun deleteAllTraces()

    fun observeTraceCount(): Flow<Int>
}