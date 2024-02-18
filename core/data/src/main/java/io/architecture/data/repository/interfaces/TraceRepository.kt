package io.architecture.data.repository.interfaces

import io.architecture.model.Trace
import kotlinx.coroutines.flow.Flow

interface TraceRepository {

    fun streamTraceBy(nodeId: String): Flow<Trace>

    fun streamTracesBy(nodeId: String): Flow<Trace>

    fun streamList(): Flow<List<Trace>>

    fun streamAndPersist(): Flow<Trace>

    fun streamCount(): Flow<Int>

    suspend fun deleteAll()

    fun streamViaNetwork(): Flow<Trace>
}