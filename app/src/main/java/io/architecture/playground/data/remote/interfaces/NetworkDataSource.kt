package io.architecture.playground.data.remote.interfaces

import io.architecture.playground.data.remote.model.NetworkNode
import io.architecture.playground.data.remote.model.NetworkRoute
import kotlinx.coroutines.flow.Flow

interface NetworkDataSource : StreamableConnectionDataSource {
    fun streamNodes(): Flow<NetworkNode>

    fun streamRoutes(): Flow<NetworkRoute>
}