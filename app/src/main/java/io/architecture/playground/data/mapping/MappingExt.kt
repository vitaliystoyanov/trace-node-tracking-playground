package io.architecture.playground.data.mapping

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.local.LocalTrace
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.model.NetworkConnectionEvent
import io.architecture.playground.data.remote.model.NetworkConnectionEventType
import io.architecture.playground.model.Trace

fun WebSocket.Event.toExternal(): NetworkConnectionEvent = when(this) {
    is WebSocket.Event.OnConnectionOpened<*> -> NetworkConnectionEvent(NetworkConnectionEventType.ConnectionOpened)
    is WebSocket.Event.OnConnectionClosed ->  NetworkConnectionEvent(NetworkConnectionEventType.ConnectionClosed)
    is WebSocket.Event.OnConnectionClosing -> NetworkConnectionEvent(NetworkConnectionEventType.ConnectionClosing)
    is WebSocket.Event.OnConnectionFailed -> NetworkConnectionEvent(NetworkConnectionEventType.ConnectionFailed)
    is WebSocket.Event.OnMessageReceived -> NetworkConnectionEvent(NetworkConnectionEventType.MessageReceived)
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
    mode = mode
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
    mode = mode
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