package io.architecture.playground.data

import io.architecture.playground.model.DiverTrace
import kotlinx.coroutines.flow.Flow

interface DiverTraceRepository {

    fun getStreamDiverTraces(): Flow<DiverTrace>

    fun getStreamDiverTraceHistory(): Flow<List<DiverTrace>>

    suspend fun removeAllTraces()
}