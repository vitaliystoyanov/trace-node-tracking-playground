package io.architecture.playground.data.mapping

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.local.LocalDiverTrace
import io.architecture.playground.data.remote.model.NetworkDiverTrace
import io.architecture.playground.data.remote.model.NetworkConnectionEvent
import io.architecture.playground.data.remote.model.NetworkConnectionEventType
import io.architecture.playground.model.DiverTrace

fun WebSocket.Event.toExternal(): NetworkConnectionEvent = when(this) {
    is WebSocket.Event.OnConnectionOpened<*> -> NetworkConnectionEvent(NetworkConnectionEventType.ConnectionOpened)
    is WebSocket.Event.OnConnectionClosed ->  NetworkConnectionEvent(NetworkConnectionEventType.ConnectionClosed)
    is WebSocket.Event.OnConnectionClosing -> NetworkConnectionEvent(NetworkConnectionEventType.ConnectionClosing)
    is WebSocket.Event.OnConnectionFailed -> NetworkConnectionEvent(NetworkConnectionEventType.ConnectionFailed)
    is WebSocket.Event.OnMessageReceived -> NetworkConnectionEvent(NetworkConnectionEventType.MessageReceived)
}

fun DiverTrace.toLocal() = LocalDiverTrace(
    id = id,
    ept = ept,
    eps = eps,
    epv = epv,
    lon = lon,
    time = time,
    epd = epd,
    epx = epx,
    speed = speed,
    alt = alt,
    epy = epy,
    track = track,
    lat = lat,
    mode = mode
)

fun LocalDiverTrace.toExternal() = DiverTrace(
    id = id,
    ept = ept,
    eps = eps,
    epv = epv,
    lon = lon,
    time = time,
    epd = epd,
    epx = epx,
    speed = speed,
    alt = alt,
    epy = epy,
    track = track,
    lat = lat,
    mode = mode
)

fun NetworkDiverTrace.toLocal() = LocalDiverTrace(
    id = id,
    ept = ept,
    eps = eps,
    epv = epv,
    lon = lon,
    time = time,
    epd = epd,
    epx = epx,
    speed = speed,
    alt = alt,
    epy = epy,
    track = track,
    lat = lat,
    mode = mode
)



fun LocalDiverTrace.toNetwork() = NetworkDiverTrace(
    id = id,
    ept = ept,
    eps = eps,
    epv = epv,
    lon = lon,
    time = time,
    epd = epd,
    epx = epx,
    speed = speed,
    alt = alt,
    epy = epy,
    track = track,
    lat = lat,
    mode = mode
)

fun DiverTrace.toNetwork() = toLocal().toNetwork()

fun NetworkDiverTrace.toExternal() = toLocal().toExternal()

fun List<LocalDiverTrace>.toNetwork() = map(LocalDiverTrace::toNetwork)

fun List<DiverTrace>.toLocal() = map(DiverTrace::toLocal)

@JvmName("localToExternal")
fun List<LocalDiverTrace>.toExternal() = map(LocalDiverTrace::toExternal)

@JvmName("networkToLocal")
fun List<NetworkDiverTrace>.toLocal() = map(NetworkDiverTrace::toLocal)

@JvmName("externalToNetwork")
fun List<DiverTrace>.toNetwork() = map(DiverTrace::toNetwork)

@JvmName("networkToExternal")
fun List<NetworkDiverTrace>.toExternal() = map(NetworkDiverTrace::toExternal)