package io.architecture.playground.data.remote.interfaces

import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.model.NetworkRoute
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource : StreamableConnectionDataSource {
    fun streamTraces(): Flow<NetworkTrace>

    fun streamRoutes(): Flow<NetworkRoute>
}