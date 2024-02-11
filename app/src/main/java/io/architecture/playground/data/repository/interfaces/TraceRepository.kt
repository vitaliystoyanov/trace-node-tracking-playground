package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.Flow

interface TraceRepository {

    fun observeList(): Flow<List<Trace>>

    fun observeAndStore(): Flow<Trace>

    fun observeCount(): Flow<Int>

    suspend fun deleteAll()
}