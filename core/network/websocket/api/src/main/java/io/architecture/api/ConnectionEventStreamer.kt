package io.architecture.api

import io.architecture.model.ConnectionEvent
import kotlinx.coroutines.flow.Flow

interface ConnectionEventStreamer {
    fun streamConnectionEvents(): Flow<ConnectionEvent>
}