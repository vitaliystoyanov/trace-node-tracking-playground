package io.architecture.playground.data.local

import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.RouteEntity
import io.architecture.playground.data.local.model.TraceEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun observeAllTraces(): Flow<List<TraceEntity>>

    fun observeAllNodes(): Flow<List<NodeEntity>>

    fun observeTraceCount(): Flow<Int>

    fun observeTraceBy(nodeId: String): Flow<TraceEntity>

    fun observeNodeCount(): Flow<Int>

    suspend fun getRouteBy(nodeId: String): RouteEntity?

    suspend fun createOrUpdate(node: NodeEntity)

    suspend fun createOrUpdate(trace: TraceEntity)

    suspend fun createOrUpdate(route: RouteEntity)

    suspend fun getAllTraces(): List<TraceEntity>

    suspend fun deleteAllTraces()
}