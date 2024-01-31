package io.architecture.playground.data.remote.websocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.playground.data.remote.model.NetworkDiverTrace
import kotlinx.coroutines.channels.ReceiveChannel

interface WebSocketDiverService {

    @Receive
    fun observeConnection(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun observeDiverTraces(): ReceiveChannel<NetworkDiverTrace>
}