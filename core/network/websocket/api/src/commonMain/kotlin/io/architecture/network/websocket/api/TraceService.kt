package io.architecture.network.websocket.api

import io.architecture.model.ConnectionEvent
import io.architecture.network.websocket.api.model.NetworkTrace
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface TraceService {
    fun streamTraces(): Flow<NetworkTrace>

    fun streamConnectionEvents(): SharedFlow<ConnectionEvent>
}