package io.architecture.playground.data.remote.websocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import io.architecture.playground.data.remote.model.NetworkNode
import kotlinx.coroutines.channels.ReceiveChannel

interface NodeService {

    @Receive
    fun streamConnection(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun streamNodes(): ReceiveChannel<NetworkNode>
}