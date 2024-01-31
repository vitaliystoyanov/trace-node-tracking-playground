package io.architecture.playground.data

import io.architecture.playground.data.remote.model.NetworkConnectionEvent
import io.architecture.playground.model.DiverTrace
import kotlinx.coroutines.flow.Flow

interface DiverTraceRepository {

    fun observeConnection(): Flow<NetworkConnectionEvent>

    fun getStreamDiverTraces(): Flow<DiverTrace>

    fun getStreamDiverTraceHistory(): Flow<List<DiverTrace>>

}