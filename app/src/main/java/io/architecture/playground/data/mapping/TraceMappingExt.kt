package io.architecture.playground.data.mapping

import com.tinder.scarlet.WebSocket
import io.architecture.playground.data.local.model.TraceEntity
import io.architecture.playground.data.remote.model.ConnectionEvent
import io.architecture.playground.data.remote.model.NetworkTrace
import io.architecture.playground.model.Trace
import java.util.Date

fun WebSocket.Event.toExternalAs(): ConnectionEvent = when (this) {
    is WebSocket.Event.OnConnectionOpened<*> -> ConnectionEvent.OPENED
    is WebSocket.Event.OnConnectionClosed -> ConnectionEvent.CLOSED
    is WebSocket.Event.OnConnectionClosing -> ConnectionEvent.CLOSING
    is WebSocket.Event.OnConnectionFailed -> ConnectionEvent.FAILED
    is WebSocket.Event.OnMessageReceived -> ConnectionEvent.MESSAGE_RECEIVED
    else -> {
        ConnectionEvent.UNDEFINED
    }
}

fun Trace.toLocal() = TraceEntity(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = sentAtTime,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

fun TraceEntity.toExternalAs() = Trace(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = sentAtTime,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

fun NetworkTrace.toLocal() = TraceEntity(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = Date(sentAtTime),
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

fun TraceEntity.toNetwork() = NetworkTrace(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = sentAtTime.time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)


fun Trace.toNetwork() = toLocal().toNetwork()

fun NetworkTrace.toExternalAs() = toLocal().toExternalAs()

fun List<TraceEntity>.toNetwork() = map(TraceEntity::toNetwork)

fun List<Trace>.toLocal() = map(Trace::toLocal)


@JvmName("localToExternal")
fun List<TraceEntity>.toExternalAs() = map(TraceEntity::toExternalAs)

@JvmName("networkToLocal")
fun List<NetworkTrace>.toLocal() = map(NetworkTrace::toLocal)

@JvmName("externalToNetwork")
fun List<Trace>.toNetwork() = map(Trace::toNetwork)

@JvmName("networkToExternal")
fun List<NetworkTrace>.toExternalAs() = map(NetworkTrace::toExternalAs)


fun TraceEntity.assignProperties(tracePooled: Trace, source: TraceEntity): Trace =
    tracePooled.apply {
        nodeId = source.nodeId
        lon = source.lon
        sentAtTime = source.sentAtTime
        speed = source.speed
        azimuth = source.azimuth
        alt = source.alt
        lat = source.lat
    }

fun Trace.assignProperties(tracePooled: TraceEntity, source: Trace): TraceEntity =
    tracePooled.apply {
        nodeId = source.nodeId
        lon = source.lon
        sentAtTime = source.sentAtTime
        speed = source.speed
        azimuth = source.azimuth
        alt = source.alt
        lat = source.lat
    }


fun NetworkTrace.assignProperties(
    tracePooled: TraceEntity, source: NetworkTrace
): TraceEntity =
    tracePooled.apply {
        nodeId = source.nodeId
        lon = source.lon
        sentAtTime = Date(source.sentAtTime)
        speed = source.speed
        azimuth = source.azimuth
        alt = source.alt
        lat = source.lat
    }