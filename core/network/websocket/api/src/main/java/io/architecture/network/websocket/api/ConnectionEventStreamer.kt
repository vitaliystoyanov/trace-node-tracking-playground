package io.architecture.network.websocket.api

import io.architecture.model.ConnectionEvent
import kotlinx.coroutines.flow.SharedFlow

interface ConnectionEventStreamer {
    fun streamConnectionEvents(): SharedFlow<ConnectionEvent>
}