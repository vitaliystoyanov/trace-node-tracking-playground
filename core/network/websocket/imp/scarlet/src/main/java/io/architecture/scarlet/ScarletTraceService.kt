package io.architecture.scarlet

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.architecture.api.TraceService
import io.architecture.api.model.NetworkClientTime
import io.architecture.api.model.NetworkServerTime
import io.architecture.api.model.NetworkTrace
import io.architecture.scarlet.internal.Target
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