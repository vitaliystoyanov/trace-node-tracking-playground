package io.architecture.playground.data.local

import io.architecture.playground.data.local.dao.NodeDao
import io.architecture.playground.data.local.dao.RouteDao
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.NodeWithRouteEntity
import io.architecture.playground.data.local.model.RouteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalNodesDataSource @Inject constructor(
    private val nodeDao: NodeDao,
    private val routeDao: RouteDao
) : LocalNodeRouteDataSource {

    override fun observeAllNodes(): Flow<List<NodeEntity>> = nodeDao.observeAll()

    override fun observeAllNodesWithRoute(): Flow<List<NodeWithRouteEntity>> =
        nodeDao.observeAllNodeWithRoute()

    override fun observeCountNodes(): Flow<Int> = nodeDao.observeCountNodes()

    override suspend fun getRouteBy(nodeId: String) = routeDao.getById(nodeId)

    override suspend fun add(node: NodeEntity) = nodeDao.insert(node)

    override suspend fun add(route: RouteEntity) = routeDao.insert(route)

    override suspend fun getAllNodes(): List<NodeEntity> = nodeDao.getAll()

    override suspend fun deleteAllNodes() = nodeDao.deleteAll()

    override suspend fun deleteAllNodesWithRoute() {
        nodeDao.deleteAll()
        routeDao.deleteAll()
    }
}