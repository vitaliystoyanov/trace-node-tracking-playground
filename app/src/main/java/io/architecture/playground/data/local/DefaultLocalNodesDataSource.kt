package io.architecture.playground.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalNodesDataSource @Inject constructor(
    private val dao: NodeDao
) : LocalNodesDataSource {

    override suspend fun add(node: LocalNode) = dao.insert(node)

    override fun observeAll(): Flow<List<LocalNode>> = dao.observeAll()

    override fun observeCountNodes(): Flow<Long> = dao.observeCountNodes()

    override fun observeNodeLatest(): Flow<LocalNode> = dao.observeLatest()

    override suspend fun getAll(): List<LocalNode> = dao.getAll()

    override fun deleteAll() = dao.deleteAll()

    override suspend fun getAllTracesByNodeId(nodeId: String): List<LocalNode> = dao.getAllBy(nodeId)

}