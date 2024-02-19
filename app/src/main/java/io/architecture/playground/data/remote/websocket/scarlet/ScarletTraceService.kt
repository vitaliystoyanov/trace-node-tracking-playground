package io.architecture.playground.data.remote.websocket.scarlet

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.architecture.playground.data.remote.model.NetworkClientTime
import io.architecture.playground.data.remote.model.NetworkServerTime
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.websocket.TraceService
import io.architecture.playground.data.remote.websocket.scarlet.internal.Target
import kotlinx.coroutines.flow.Flow


interface ScarletTraceService : TraceService {

    @Receive
    fun streamConnection(): Flow<WebSocket.Event>

    @Receive
    @Target(type = "trace")
    override fun streamTraces(): Flow<NetworkTrace>

    @Send
    fun sendClientTime(time: NetworkClientTime)

    @Receive
    @Target(type = "rtt") // deserialization by provided type that includes in incoming Message.TEXT format
    fun streamServerTime(): Flow<NetworkServerTime>
}