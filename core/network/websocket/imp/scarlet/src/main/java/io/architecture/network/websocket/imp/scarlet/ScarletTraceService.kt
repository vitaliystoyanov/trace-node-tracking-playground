package io.architecture.network.websocket.imp.scarlet

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.architecture.network.websocket.api.TraceService
import io.architecture.network.websocket.api.model.NetworkTrace
import io.architecture.network.websocket.imp.scarlet.internal.Target
import io.architecture.network.websocket.model.NetworkClientTime
import io.architecture.network.websocket.model.NetworkServerTime
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