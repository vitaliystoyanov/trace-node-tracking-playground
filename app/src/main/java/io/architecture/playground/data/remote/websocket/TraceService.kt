package io.architecture.playground.data.remote.websocket

import io.architecture.playground.data.remote.model.NetworkTrace
import kotlinx.coroutines.flow.Flow

interface TraceService {
    fun streamTraces(): Flow<NetworkTrace>
}