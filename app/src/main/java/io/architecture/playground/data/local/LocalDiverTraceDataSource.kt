package io.architecture.playground.data.local

import kotlinx.coroutines.flow.Flow

interface LocalDiverTraceDataSource {

    suspend fun add(trace: LocalDiverTrace)

    fun observeAll(): Flow<List<LocalDiverTrace>>

    suspend fun getAll(): List<LocalDiverTrace>

    fun observeTraceLatest(): Flow<LocalDiverTrace>
}