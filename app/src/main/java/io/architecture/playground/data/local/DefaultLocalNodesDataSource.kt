package io.architecture.playground.data.local

import io.architecture.playground.data.local.dao.NodeDao
import io.architecture.playground.data.local.dao.RouteDao
import io.architecture.playground.data.local.model.LocalNode
import io.architecture.playground.data.local.model.LocalNodeWithRoute
import io.architecture.playground.data.local.model.LocalRoute
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalNodesDataSource @Inject constructor(
    private val nodeDao: NodeDao,
    private val routeDao: RouteDao
) : LocalNodeRouteDataSource {
    override suspend fun getRouteBy(nodeId: String) = routeDao.getById(nodeId)

    override suspend fun add(node: LocalNode) = nodeDao.insert(node)

    override suspend fun add(route: LocalRoute) = routeDao.insert(route)

    override fun observeAllNodes(): Flow<List<LocalNode>> = nodeDao.observeAll()

    override fun observeAllNodesWithRoute(): Flow<List<LocalNodeWithRoute>> =
        nodeDao.observeAllNodeWithRoute()

    override fun observeCountNodes(): Flow<Long> = nodeDao.observeCountNodes()

    override fun observeLatestNode(): Flow<LocalNode> = nodeDao.observeLatest()

    override suspend fun getAllNodes(): List<LocalNode> = nodeDao.getAll()

    override suspend fun deleteAllNodes() = nodeDao.deleteAll()

    override suspend fun deleteAllNodesWithRoute() {
        nodeDao.deleteAll()
        routeDao.deleteAll()
    }

    override suspend fun getAllTracesByNodeId(nodeId: String): List<LocalNode> =
        nodeDao.getAllBy(nodeId)
}