package io.architecture.data.mapping

//import com.tinder.scarlet.WebSocket
import io.architecture.api.model.NetworkTrace
import io.architecture.database.api.model.TraceEntity
import io.architecture.model.Node
import io.architecture.model.Trace
import java.util.Date

//fun WebSocket.Event.toExternalAs(): io.architecture.model.ConnectionEvent = when (this) {
//    is WebSocket.Event.OnConnectionOpened<*> -> io.architecture.model.ConnectionEvent.OPENED
//    is WebSocket.Event.OnConnectionClosed -> io.architecture.model.ConnectionEvent.CLOSED
//    is WebSocket.Event.OnConnectionClosing -> io.architecture.model.ConnectionEvent.CLOSING
//    is WebSocket.Event.OnConnectionFailed -> io.architecture.model.ConnectionEvent.FAILED
//    is WebSocket.Event.OnMessageReceived -> io.architecture.model.ConnectionEvent.MESSAGE_RECEIVED
//    else -> {
//        io.architecture.model.ConnectionEvent.UNDEFINED
//    }
//}

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

fun Trace.toNode() = Node(id = nodeId, mode = 1) // TODO mode is always ACTIVE


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