package io.architecture.data.repository.interfaces

import io.architecture.model.Node
import kotlinx.coroutines.flow.Flow

interface NodeRepository {

    suspend fun createOrUpdate(node: Node)

    fun streamCount(): Flow<Int>

    fun streamAllNodes(): Flow<List<Node>>
}