package io.architecture.playground.data.local

import io.architecture.playground.data.local.dao.NodeDao
import io.architecture.playground.data.local.dao.RouteDao
import io.architecture.playground.data.local.dao.TraceDao
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.NodeWithLastTraceEntity
import io.architecture.playground.data.local.model.RouteEntity
import io.architecture.playground.data.local.model.TraceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InMemoryLocalDataSource @Inject constructor(
    private val nodeDao: NodeDao,
    private val traceDao: TraceDao,
    private val routeDao: RouteDao
) : LocalDataSource {

    override fun observeAllNodes(): Flow<List<NodeEntity>> = nodeDao.observeAll()

    override fun observeAllNodesWithLatestTrace(): Flow<List<NodeWithLastTraceEntity>> = nodeDao.observeAllWithLastTrace()

    override fun observeAllTraces(): Flow<List<TraceEntity>> = traceDao.observeAll()


    override fun observeNodeCount(): Flow<Int> = nodeDao.observeCount()

    override suspend fun getRouteBy(nodeId: String) = routeDao.getById(nodeId)

    override suspend fun updateOrCreate(node: NodeEntity) = nodeDao.insert(node)

    override suspend fun updateOrCreate(trace: TraceEntity) = traceDao.insert(trace)

    override suspend fun updateOrCreate(route: RouteEntity) = routeDao.insert(route)

    override suspend fun getAllTraces(): List<TraceEntity> = traceDao.getAll()

    override suspend fun deleteAllTraces() = traceDao.deleteAll()

    override suspend fun deleteAllWithRoute() {
        traceDao.deleteAll()
        routeDao.deleteAll()
    }
}