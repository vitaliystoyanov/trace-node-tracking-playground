package io.architecture.playground.data.remote.websocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.playground.data.remote.model.NetworkTrace
import kotlinx.coroutines.channels.ReceiveChannel

interface WebSocketTraceService {

    @Receive
    fun observeConnection(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun observeTraces(): ReceiveChannel<NetworkTrace>
}