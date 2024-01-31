package io.architecture.playground.data.local

import kotlinx.coroutines.flow.Flow

interface LocalDiverTraceDataSource {

    suspend fun add(trace: LocalDiverTrace)
    fun observeAll(): Flow<List<LocalDiverTrace>>

}