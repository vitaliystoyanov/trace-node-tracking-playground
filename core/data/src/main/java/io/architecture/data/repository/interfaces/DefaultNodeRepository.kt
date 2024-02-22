package io.architecture.data.repository.interfaces

import io.architecture.database.api.model.toExternal
import io.architecture.database.api.model.toLocal
import io.architecture.datasource.api.LocalDataSource
import io.architecture.model.Node
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

class DefaultNodeRepository(
    private val localDataSource: LocalDataSource,
    @Named("ioDispatcher") private val ioDispatcher: CoroutineDispatcher,
) : NodeRepository {

    override suspend fun createOrUpdate(node: Node): Unit = withContext(ioDispatcher) {
        localDataSource.createOrUpdate(node.toLocal())
    }

    override fun streamAllNodes(): Flow<List<Node>> =
        localDataSource.observeAllNodes().map { it.toExternal() }

    override fun streamCount(): Flow<Int> = localDataSource.observeNodeCount()

}