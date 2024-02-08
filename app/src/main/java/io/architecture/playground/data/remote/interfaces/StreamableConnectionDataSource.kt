package io.architecture.playground.data.remote.interfaces

import io.architecture.playground.data.remote.model.ConnectionState
import kotlinx.coroutines.flow.Flow

interface StreamableConnectionDataSource {
    fun streamConnection(): Flow<ConnectionState>
}