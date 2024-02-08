package io.architecture.playground.data.mapping

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.local.model.TraceEntity
import io.architecture.playground.data.remote.model.ConnectionState
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.data.remote.model.SocketConnectionState
import io.architecture.playground.model.Trace
import io.architecture.playground.model.NodeMode
import java.util.Date

fun WebSocket.Event.toExternal(): ConnectionState = when (this) {
    is WebSocket.Event.OnConnectionOpened<*> -> ConnectionState(SocketConnectionState.OPENED)
    is WebSocket.Event.OnConnectionClosed -> ConnectionState(SocketConnectionState.CLOSED)
    is WebSocket.Event.OnConnectionClosing -> ConnectionState(SocketConnectionState.CLOSING)
    is WebSocket.Event.OnConnectionFailed -> ConnectionState(SocketConnectionState.FAILED)
    is WebSocket.Event.OnMessageReceived -> ConnectionState(SocketConnectionState.MESSAGE_RECEIVED)
}

fun Trace.toLocal() = TraceEntity(
    nodeId = nodeId,
    lon = lon,
    time = time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

fun TraceEntity.toExternal() = Trace(
    nodeId = nodeId,
    lon = lon,
    time = time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

fun NetworkTrace.toLocal() = TraceEntity(
    nodeId = nodeId,
    lon = lon,
    time = Date(time),
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

fun TraceEntity.toNetwork() = NetworkTrace(
    nodeId = nodeId,
    lon = lon,
    time = time.time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

fun Trace.toExternal(source: TraceEntity, destination: Trace): Trace {
    destination.nodeId = source.nodeId
    destination.lon = source.lon
    destination.time = source.time
    destination.speed = source.speed
    destination.azimuth = source.azimuth
    destination.alt = source.alt
    destination.lat = source.lat
//    destination.mode = NodeMode.entries.first { it.valueInt == source.mode }
    return destination
}

fun Trace.toNetwork() = toLocal().toNetwork()

fun NetworkTrace.toExternal() = toLocal().toExternal()

fun List<TraceEntity>.toNetwork() = map(TraceEntity::toNetwork)

fun List<Trace>.toLocal() = map(Trace::toLocal)

@JvmName("localToExternal")
fun List<TraceEntity>.toExternal() = map(TraceEntity::toExternal)

@JvmName("networkToLocal")
fun List<NetworkTrace>.toLocal() = map(NetworkTrace::toLocal)

@JvmName("externalToNetwork")
fun List<Trace>.toNetwork() = map(Trace::toNetwork)

@JvmName("networkToExternal")
fun List<NetworkTrace>.toExternal() = map(NetworkTrace::toExternal)