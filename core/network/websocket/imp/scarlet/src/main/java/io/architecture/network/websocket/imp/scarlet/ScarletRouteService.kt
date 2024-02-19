package io.architecture.network.websocket.imp.scarlet

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.network.websocket.api.RouteService
import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.network.websocket.imp.scarlet.internal.Target
import kotlinx.coroutines.flow.Flow

internal interface ScarletRouteService : RouteService {
    @Receive
    fun streamConnection(): Flow<WebSocket.Event>

    @Receive
    @Target(type = "route")
    override fun streamRoutes(): Flow<NetworkRoute>
}