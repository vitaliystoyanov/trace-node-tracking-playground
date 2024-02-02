package io.architecture.playground.data

import io.architecture.playground.data.remote.model.NetworkConnectionEvent
import io.architecture.playground.model.Trace
import kotlinx.coroutines.flow.Flow

interface TraceRepository {

    fun getStreamConnectionEvents(): Flow<NetworkConnectionEvent>

    fun getStreamTraces(): Flow<Trace>

    fun getStreamCountTraces(): Flow<Long>

    fun deleteAllTraces()

    fun getStreamTraceHistory(): Flow<List<Trace>>

    fun getStreamLatestTraceByUniqNodeIds(): Flow<List<Trace>>

}