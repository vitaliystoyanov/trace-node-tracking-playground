package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.data.local.model.NodeWithLastTraceEntity
import io.architecture.playground.model.Node
import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.Flow

interface NodeRepository {
    suspend fun updateOrAdd(node: Node)

    fun observeNodesWithLastTrace(): Flow<Set<Pair<Node, Trace>>>
}