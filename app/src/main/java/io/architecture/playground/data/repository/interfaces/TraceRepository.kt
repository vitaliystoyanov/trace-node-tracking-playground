package io.architecture.playground.data.repository.interfaces

import io.architecture.playground.data.local.model.TraceEntity
import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface TraceRepository {

    val sharedStreamTraces: SharedFlow<Trace>

    fun streamTraceBy(nodeId: String): Flow<Trace>

    fun streamTracesBy(nodeId: String): Flow<TraceEntity>

    fun streamList(): Flow<List<Trace>>

    fun streamAndPersist(): Flow<Trace>

    fun streamCount(): Flow<Int>

    suspend fun deleteAll()

    fun streamViaNetwork(): Flow<Trace>
}