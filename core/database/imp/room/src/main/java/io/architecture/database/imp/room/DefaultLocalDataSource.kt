package io.architecture.database.imp.room

import io.architecture.database.api.dao.NodeDao
import io.architecture.database.api.dao.RouteDao
import io.architecture.database.api.dao.TraceDao
import io.architecture.database.api.model.NodeEntity
import io.architecture.database.api.model.RouteEntity
import io.architecture.database.api.model.TraceEntity
import io.architecture.datasource.api.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalDataSource @Inject constructor(
    private val nodeDao: NodeDao,
    private val traceDao: TraceDao,
    private val routeDao: RouteDao
) : LocalDataSource {

    override fun observeAllNodes(): Flow<List<NodeEntity>> = nodeDao.observeAll()

    override fun observeAllTraces(): Flow<List<TraceEntity>> = traceDao.observeAll()

    override fun observeNodeCount(): Flow<Int> = nodeDao.observeCount()

    override suspend fun getRouteBy(nodeId: String) = routeDao.getById(nodeId)

    override suspend fun createOrUpdate(node: NodeEntity) = nodeDao.insert(node)

    override suspend fun createOrUpdate(trace: TraceEntity) = traceDao.insert(trace)

    override suspend fun createOrUpdate(route: RouteEntity) = routeDao.insert(route)

    override suspend fun getAllTraces(): List<TraceEntity> = traceDao.getAll()

    override suspend fun deleteAllTraces() = traceDao.deleteAll()

    override fun observeTraceCount(): Flow<Int> = traceDao.observeCount()

    override fun observeTraceBy(nodeId: String) = traceDao.observeById(nodeId)
}