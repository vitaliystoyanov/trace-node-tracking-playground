package io.architecture.playground.data.repository

import io.architecture.playground.data.local.LocalDataSource
import io.architecture.playground.data.mapping.toExternalPairs
import io.architecture.playground.data.mapping.toLocal
import io.architecture.playground.data.repository.interfaces.NodeRepository
import io.architecture.playground.di.DefaultDispatcher
import io.architecture.playground.di.IoDispatcher
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultNodeRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NodeRepository {

    override suspend fun updateOrAdd(node: Node) = withContext(ioDispatcher) {
        localDataSource.updateOrCreate(node.toLocal())
    }


    override fun observeNodesWithLastTrace(): Flow<Set<Pair<Node, Trace>>> =
        localDataSource.observeAllNodesWithLatestTrace()
            .map { it.toExternalPairs() }
}