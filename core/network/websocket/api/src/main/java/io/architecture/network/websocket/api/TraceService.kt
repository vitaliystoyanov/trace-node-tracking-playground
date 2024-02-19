package io.architecture.network.websocket.api

import io.architecture.network.websocket.api.model.NetworkTrace
import kotlinx.coroutines.flow.Flow

interface TraceService {
    fun streamTraces(): Flow<NetworkTrace>
}