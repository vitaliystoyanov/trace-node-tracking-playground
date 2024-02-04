package io.architecture.playground.data

import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.Flow

interface TraceRepository {

    fun getStreamConnectionState(): Flow<ConnectionState>

    fun getStreamTraces(): Flow<Trace>

    fun getStreamCountTraces(): Flow<Long>

    fun deleteAllTraces()

    fun getStreamTraceHistory(): Flow<List<Trace>>

    fun getStreamLatestTraceByUniqNodeIds(): Flow<List<Trace>>

    suspend fun getAllTracesByNodeId(nodeId: String): List<Trace>

}