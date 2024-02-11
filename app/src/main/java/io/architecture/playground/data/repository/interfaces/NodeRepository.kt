package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.model.CompositeNodeTrace
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.Flow

interface NodeRepository {
    suspend fun updateOrAdd(node: Node)

    fun observeNodesWithLastTrace(): Flow<Sequence<CompositeNodeTrace>>

    suspend fun getNodesWithLastTrace(): Map<Node, Trace>

    fun observeCount(): Flow<Int>
}