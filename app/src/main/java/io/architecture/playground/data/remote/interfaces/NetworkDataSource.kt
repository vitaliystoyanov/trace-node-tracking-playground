package io.architecture.playground.data.remote.interfaces

import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.model.NetworkTrace
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource {
    fun streamTraces(): Flow<NetworkTrace>

    fun streamRoutes(): Flow<NetworkRoute>

    fun streamTraceConnectionState(): Flow<ConnectionEvent>

    fun streamRouteConnectionState(): Flow<ConnectionEvent>
}