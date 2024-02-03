package io.architecture.playground.data.local

import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalTraceDataSource @Inject constructor(
    private val dao: TraceDao
) : LocalTraceDataSource {
    override suspend fun add(trace: LocalTrace) = dao.insert(trace)

    override fun observeAll(): Flow<List<LocalTrace>> = dao.observeAll()

    override fun observeCountTraces(): Flow<Long> = dao.observeCountTraces()

    override fun observeTraceLatest(): Flow<LocalTrace> = dao.observeLatest()

    override fun observeLatestTraceByUniqNodeIds(): Flow<List<LocalTrace>> =
        dao.observeLatestTraceByUniqNodeIds()

    override suspend fun getAll(): List<LocalTrace> = dao.getAll()
    override fun deleteAllTraces() = dao.deleteAll()
    suspend fun getAllTracesByNodeId(nodeId: String): List<LocalTrace> = dao.getAllById(nodeId)

}