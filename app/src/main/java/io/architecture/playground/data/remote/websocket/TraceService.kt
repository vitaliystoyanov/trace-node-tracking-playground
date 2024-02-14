package io.architecture.playground.data.remote.websocket

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.websocket.scarlet.Target
import kotlinx.coroutines.channels.ReceiveChannel

interface TraceService {

    @Receive
    fun streamConnection(): ReceiveChannel<WebSocket.Event>

    @Receive
    @Target(type = "trace")
    fun streamTraces(): ReceiveChannel<NetworkTrace>

    @Send
    fun sendClientTime(time: NetworkClientTime)

    @Receive
    @Target(type = "rtt") // deserialization by provided type that includes in incoming Message.TEXT format
    fun streamServerTime(): ReceiveChannel<NetworkServerTime>
}