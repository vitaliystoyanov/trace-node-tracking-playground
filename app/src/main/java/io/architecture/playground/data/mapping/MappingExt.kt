package io.architecture.playground.data.mapping

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.local.LocalTrace
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.model.NodeMode
import io.architecture.playground.model.Trace

fun WebSocket.Event.toExternal(): ConnectionState = when(this) {
    is WebSocket.Event.OnConnectionOpened<*> -> ConnectionState(SocketConnectionState.OPENED)
    is WebSocket.Event.OnConnectionClosed ->  ConnectionState(SocketConnectionState.CLOSED)
    is WebSocket.Event.OnConnectionClosing -> ConnectionState(SocketConnectionState.CLOSING)
    is WebSocket.Event.OnConnectionFailed -> ConnectionState(SocketConnectionState.FAILED)
    is WebSocket.Event.OnMessageReceived -> ConnectionState(SocketConnectionState.MESSAGE_RECEIVED)
}

fun Trace.toLocal() = LocalTrace(
    id = id,
    lon = lon,
    time = time,
    speed = speed,
    bearing = bearing,
    alt = alt,
    lat = lat,
    nodeId = nodeId,
    mode = mode.valueInt
)

fun LocalTrace.toExternal() = Trace(
    id = id,
    lon = lon,
    time = time,
    speed = speed,
    bearing = bearing,
    alt = alt,
    lat = lat,
    nodeId = nodeId,
    mode = NodeMode.entries.first { it.valueInt == mode }
)

fun NetworkTrace.toLocal() = LocalTrace(
    id = 0,
    lon = lon,
    time = time,
    speed = speed,
    bearing = bearing,
    alt = alt,
    lat = lat,
    nodeId = nodeId,
    mode = mode
)

fun LocalTrace.toNetwork() = NetworkTrace(
    lon = lon,
    time = time,
    speed = speed,
    bearing = bearing,
    alt = alt,
    lat = lat,
    nodeId = nodeId,
    mode = mode
)

fun Trace.toNetwork() = toLocal().toNetwork()

fun NetworkTrace.toExternal() = toLocal().toExternal()

fun List<LocalTrace>.toNetwork() = map(LocalTrace::toNetwork)

fun List<Trace>.toLocal() = map(Trace::toLocal)

@JvmName("localToExternal")
fun List<LocalTrace>.toExternal() = map(LocalTrace::toExternal)

@JvmName("networkToLocal")
fun List<NetworkTrace>.toLocal() = map(NetworkTrace::toLocal)

@JvmName("externalToNetwork")
fun List<Trace>.toNetwork() = map(Trace::toNetwork)

@JvmName("networkToExternal")
fun List<NetworkTrace>.toExternal() = map(NetworkTrace::toExternal)