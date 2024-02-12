package io.architecture.playground.data.remote.websocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.data.remote.websocket.scarlet.Target
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.Flow

interface RoutesService {
    @Receive
    @Target(type = "")
    fun streamConnection(): ReceiveChannel<WebSocket.Event>

    @Receive
    @Target(type = "route")
    fun streamRoutes(): ReceiveChannel<NetworkRoute>
}