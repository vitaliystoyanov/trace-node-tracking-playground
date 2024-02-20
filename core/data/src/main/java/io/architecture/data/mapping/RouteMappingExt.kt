package io.architecture.data.mapping

import io.architecture.database.api.model.CoordinateEntity
import io.architecture.database.api.model.RouteEntity
import io.architecture.database.api.model.toExternal
import io.architecture.model.Route
import io.architecture.network.websocket.api.model.NetworkRoute


internal fun NetworkRoute.toLocal() = RouteEntity(
    nodeId = nodeId,
    route = route?.map { coordinate ->
        CoordinateEntity(coordinate[0], coordinate[1])
    } ?: emptyList()
)

internal fun NetworkRoute.toExternal(): Route = toLocal().toExternal()

@JvmName("networkToLocal")
internal fun List<NetworkRoute>.toLocal() = map(NetworkRoute::toLocal)

@JvmName("networkToExternal")
internal fun List<NetworkRoute>.toExternal() = map(NetworkRoute::toExternal)