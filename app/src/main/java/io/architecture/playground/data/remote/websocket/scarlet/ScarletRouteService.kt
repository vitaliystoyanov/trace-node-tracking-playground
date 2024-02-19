package io.architecture.playground.data.remote.websocket.scarlet

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.websocket.RouteService
import io.architecture.playground.data.remote.websocket.scarlet.internal.Target
import kotlinx.coroutines.flow.Flow

interface ScarletRouteService : RouteService {
    @Receive
    fun streamConnection(): Flow<WebSocket.Event>

    @Receive
    @Target(type = "route")
    override fun streamRoutes(): Flow<NetworkRoute>
}