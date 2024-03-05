package io.architecture.datasource.api

import io.architecture.database.api.model.NodeEntity
import io.architecture.database.api.model.RouteEntity
import io.architecture.database.api.model.TraceEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun observeAllTraces(): Flow<List<TraceEntity>>

    fun observeAllNodes(): Flow<List<NodeEntity>>

    fun observeTraceCount(): Flow<Int>

    fun observeTraceBy(nodeId: String): Flow<TraceEntity>

    fun observeNodeCount(): Flow<Int>

    suspend fun getRouteBy(nodeId: String): RouteEntity?

    suspend fun deleteAllTraces()

    suspend fun createOrUpdate(trace: TraceEntity)

    suspend fun createOrUpdate(route: RouteEntity)

    suspend fun createOrUpdate(node: NodeEntity)
}