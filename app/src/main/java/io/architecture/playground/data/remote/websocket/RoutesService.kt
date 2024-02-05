package io.architecture.playground.data.remote.websocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.playground.data.remote.model.NetworkRoute
import kotlinx.coroutines.channels.ReceiveChannel

interface RoutesService {
    @Receive
    fun streamConnection(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun streamRoutes(): ReceiveChannel<NetworkRoute>
}