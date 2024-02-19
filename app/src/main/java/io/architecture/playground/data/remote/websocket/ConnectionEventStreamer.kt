package io.architecture.playground.data.remote.websocket

import io.architecture.playground.data.remote.model.ConnectionEvent
import kotlinx.coroutines.flow.Flow

interface ConnectionEventStreamer {
    fun streamConnectionEvents(): Flow<ConnectionEvent>
}