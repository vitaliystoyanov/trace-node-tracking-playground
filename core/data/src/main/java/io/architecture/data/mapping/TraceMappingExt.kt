package io.architecture.data.mapping

import io.architecture.network.websocket.api.model.NetworkTrace
import io.architecture.database.api.model.TraceEntity
import io.architecture.model.Node
import io.architecture.model.Trace
import java.util.Date

internal fun Trace.toLocal() = TraceEntity(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = sentAtTime,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

internal fun TraceEntity.toExternalAs() = Trace(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = sentAtTime,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

internal fun NetworkTrace.toLocal() = TraceEntity(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = Date(sentAtTime),
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

internal fun TraceEntity.toNetwork() = NetworkTrace(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = sentAtTime.time,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

internal fun Trace.toNode() = Node(id = nodeId, mode = 1) // TODO mode is always ACTIVE


internal fun Trace.toNetwork() = toLocal().toNetwork()

internal fun NetworkTrace.toExternalAs() = toLocal().toExternalAs()

internal fun List<TraceEntity>.toNetwork() = map(TraceEntity::toNetwork)

internal fun List<Trace>.toLocal() = map(Trace::toLocal)


@JvmName("localToExternal")
internal fun List<TraceEntity>.toExternalAs() = map(TraceEntity::toExternalAs)

@JvmName("networkToLocal")
internal fun List<NetworkTrace>.toLocal() = map(NetworkTrace::toLocal)

@JvmName("externalToNetwork")
internal fun List<Trace>.toNetwork() = map(Trace::toNetwork)

@JvmName("networkToExternal")
internal fun List<NetworkTrace>.toExternalAs() = map(NetworkTrace::toExternalAs)


internal fun TraceEntity.assignProperties(tracePooled: Trace, source: TraceEntity): Trace =
    tracePooled.apply {
        nodeId = source.nodeId
        lon = source.lon
        sentAtTime = source.sentAtTime
        speed = source.speed
        azimuth = source.azimuth
        alt = source.alt
        lat = source.lat
    }

internal fun Trace.assignProperties(tracePooled: TraceEntity, source: Trace): TraceEntity =
    tracePooled.apply {
        nodeId = source.nodeId
        lon = source.lon
        sentAtTime = source.sentAtTime
        speed = source.speed
        azimuth = source.azimuth
        alt = source.alt
        lat = source.lat
    }


internal fun NetworkTrace.assignProperties(
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