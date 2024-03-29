package io.architecture.data.mapping

import io.architecture.database.api.model.TraceEntity
import io.architecture.model.Node
import io.architecture.model.Trace
import io.architecture.network.websocket.api.model.NetworkTrace
import java.util.Date


internal fun Trace.toNode() = Node(id = nodeId, mode = 1) // TODO mode is always ACTIVE

internal fun NetworkTrace.toLocal() = TraceEntity(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = Date(sentAtTime),
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

internal fun NetworkTrace.toExternal() = toLocal().toExternalAs()
