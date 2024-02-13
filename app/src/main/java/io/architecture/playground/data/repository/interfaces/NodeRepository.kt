package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.model.Node
import kotlinx.coroutines.flow.Flow

interface NodeRepository {

    suspend fun createOrUpdate(node: Node)

    fun streamCount(): Flow<Int>

    fun streamAllNodes(): Flow<List<Node>>
}