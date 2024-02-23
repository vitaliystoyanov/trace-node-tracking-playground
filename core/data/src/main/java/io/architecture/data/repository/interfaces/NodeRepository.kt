package io.architecture.data.repository.interfaces

import io.architecture.model.Node
import kotlinx.coroutines.flow.Flow

interface NodeRepository {

    suspend fun createOrUpdate(node: Node)

    fun streamCount(): Flow<Int>

    fun streamNodes(): Flow<List<Node>>
}