package io.architecture.data

import io.architecture.data.repository.interfaces.NodeRepository
import io.architecture.database.api.model.toExternal
import io.architecture.database.api.model.toLocal
import io.architecture.datasource.api.LocalDataSource
import io.architecture.model.Node
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultNodeRepository(
    private val localDataSource: LocalDataSource,
    private val ioDispatcher: CoroutineDispatcher,
) : NodeRepository {

    override suspend fun createOrUpdate(node: Node): Unit = withContext(ioDispatcher) {
        localDataSource.createOrUpdate(node.toLocal())
    }

    override fun streamNodes(): Flow<List<Node>> =
        localDataSource.observeAllNodes().map { it.toExternal() }

    override fun streamCount(): Flow<Int> = localDataSource.observeNodeCount()

}