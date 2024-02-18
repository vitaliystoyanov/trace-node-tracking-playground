package io.architecture.scarlet

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.api.RouteService
import io.architecture.api.model.NetworkRoute
import io.architecture.scarlet.internal.Target
import kotlinx.coroutines.flow.Flow

interface ScarletRouteService : RouteService {
    @Receive
    fun streamConnection(): Flow<WebSocket.Event>

    @Receive
    @Target(type = "route")
    override fun streamRoutes(): Flow<NetworkRoute>
}