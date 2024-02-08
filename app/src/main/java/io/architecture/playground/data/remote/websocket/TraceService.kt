package io.architecture.playground.data.remote.websocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.playground.data.remote.model.NetworkTrace
import kotlinx.coroutines.channels.ReceiveChannel

interface TraceService {

    @Receive
    fun streamConnection(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun streamTraces(): ReceiveChannel<NetworkTrace>
}