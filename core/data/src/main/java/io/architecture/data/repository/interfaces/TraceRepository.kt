package io.architecture.data.repository.interfaces

import io.architecture.model.Trace
import kotlinx.coroutines.flow.Flow

interface TraceRepository {

    fun streamTraceBy(nodeId: String): Flow<Trace>

    fun streamTracesBy(nodeId: String): Flow<Trace>

    fun streamList(): Flow<List<Trace>>

    fun streamCount(): Flow<Int>

    suspend fun deleteAll()

    fun streamTraces(isPersisted: Boolean): Flow<Trace>
}