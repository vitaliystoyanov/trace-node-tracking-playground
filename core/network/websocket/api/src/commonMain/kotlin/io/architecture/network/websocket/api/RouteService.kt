package io.architecture.network.websocket.api

import io.architecture.network.websocket.api.model.NetworkRoute
import kotlinx.coroutines.flow.Flow


interface RouteService {
    fun streamRoutes(): Flow<NetworkRoute>
}