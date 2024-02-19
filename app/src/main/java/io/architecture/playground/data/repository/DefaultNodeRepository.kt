package io.architecture.playground.data.repository

import io.architecture.playground.core.pool.PoolManager
import io.architecture.playground.data.local.LocalDataSource
import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.mapping.assignProperties
import io.architecture.playground.data.mapping.toExternalAs
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Node
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultNodeRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    poolManager: PoolManager,
) : NodeRepository {

    private val poolNodeEntities = poolManager.getPoolByMember<NodeEntity>()

    override suspend fun createOrUpdate(node: Node): Unit = withContext(ioDispatcher) {
        val nodeEntityPooled = poolNodeEntities.acquire()
        localDataSource.createOrUpdate(assignProperties(nodeEntityPooled, node))
        poolNodeEntities.release(nodeEntityPooled)
    }

    override fun streamAllNodes(): Flow<List<Node>> =
        localDataSource.observeAllNodes().map { it.toExternalAs() }

    override fun streamCount(): Flow<Int> = localDataSource.observeNodeCount()

}