package io.architecture.playground.data.local

import kotlinx.coroutines.flow.Flow

interface LocalTraceDataSource {

    suspend fun add(trace: LocalTrace)

    fun observeAll(): Flow<List<LocalTrace>>

    fun observeCountTraces(): Flow<Long>

    suspend fun getAll(): List<LocalTrace>

    fun deleteAllTraces()

    fun observeTraceLatest(): Flow<LocalTrace>

    fun observeLatestTraceByUniqNodeIds(): Flow<List<LocalTrace>>

}