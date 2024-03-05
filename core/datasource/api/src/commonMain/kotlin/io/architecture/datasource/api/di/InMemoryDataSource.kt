package io.architecture.datasource.api.di

import io.architecture.database.api.model.NodeEntity
import io.architecture.database.api.model.RouteEntity
import io.architecture.database.api.model.TraceEntity
import io.architecture.database.api.model.toExternal
import io.architecture.database.api.model.toLocal
import io.architecture.datasource.api.LocalDataSource
import io.architecture.model.Node
import io.architecture.model.Route
import io.architecture.model.Trace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class)
val inMemoryLocalDatasourceModule = module {
    single {
        object : LocalDataSource {
            val tracesMem = LinkedHashMap<String, Trace>()
            val traceCountFlow = MutableSharedFlow<Int>()
            val tracesFlow = MutableSharedFlow<List<TraceEntity>>()

            val nodesMem = LinkedHashMap<String, Node>()
            val nodeCountFlow = MutableSharedFlow<Int>()
            val nodesFlow = MutableSharedFlow<List<NodeEntity>>()

            val routesMem = LinkedHashMap<String, Route>()
            val routesFlow = MutableSharedFlow<List<RouteEntity>>()

            override fun observeAllTraces(): Flow<List<TraceEntity>> = tracesFlow
            override fun observeAllNodes(): Flow<List<NodeEntity>> = nodesFlow
            override fun observeTraceCount(): Flow<Int> = traceCountFlow
            override fun observeTraceBy(nodeId: String): Flow<TraceEntity> =
                tracesFlow.flatMapLatest { it.asFlow() }
                    .distinctUntilChangedBy { it.sentAtTime }
                    .filter { it.nodeId == nodeId }
                    .onEach { println(it.toString()) }

            override fun observeNodeCount(): Flow<Int> = nodeCountFlow

            override suspend fun getRouteBy(nodeId: String): RouteEntity? =
                routesMem[nodeId]?.toLocal()

            override suspend fun deleteAllTraces() = tracesMem.clear()

            override suspend fun createOrUpdate(trace: TraceEntity) {
                tracesMem[trace.nodeId] = trace.toExternal()
                tracesFlow.emit(tracesMem.values.map { it.toLocal() }.toList())
                createOrUpdate(NodeEntity(trace.nodeId, 1)) // TODO Always moving
                traceCountFlow.emit(tracesMem.size)
            }

            override suspend fun createOrUpdate(route: RouteEntity) {
                routesMem[route.nodeId] = route.toExternal()
                routesFlow.emit(routesMem.values.map { it.toLocal() }.toList())
            }

            override suspend fun createOrUpdate(node: NodeEntity) {
                nodesMem[node.id] = node.toExternal()
                nodesFlow.emit(nodesMem.values.map { it.toLocal() }.toList())
                nodeCountFlow.emit(nodesMem.size)
            }
        }
    } bind LocalDataSource::class
}