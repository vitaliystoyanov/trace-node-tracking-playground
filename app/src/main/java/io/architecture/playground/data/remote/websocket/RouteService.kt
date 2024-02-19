package io.architecture.playground.data.remote.websocket

import io.architecture.playground.data.remote.model.NetworkRoute
import kotlinx.coroutines.flow.Flow


interface RouteService {
    fun streamRoutes(): Flow<NetworkRoute>
}