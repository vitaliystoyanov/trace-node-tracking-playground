package io.architecture.playground.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocalDiverTraceDataSource  @Inject constructor(
    private val dao: DiverTraceDao
) : LocalDiverTraceDataSource {
    override suspend fun add(trace: LocalDiverTrace) = dao.insert(trace)

    override fun observeAll(): Flow<List<LocalDiverTrace>> = dao.observeAll()
}