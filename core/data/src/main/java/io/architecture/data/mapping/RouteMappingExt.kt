package io.architecture.data.mapping

import io.architecture.database.api.model.CoordinateEntity
import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.database.api.model.RouteEntity
import io.architecture.model.Coordinate
import io.architecture.model.Route

internal fun Route.toLocal() = RouteEntity(
    nodeId = nodeId,
    route = coordinates.map { CoordinateEntity(it.lat, it.lon) }
)

internal fun RouteEntity.toExternalAs() = Route(
    nodeId = nodeId,
    coordinates = route?.map { Coordinate(it.lat, it.lon) } ?: emptyList()
)

internal fun NetworkRoute.toLocal() = RouteEntity(
    nodeId = nodeId,
    route = route?.map { coordinate ->
        CoordinateEntity(coordinate[0], coordinate[1])
    } ?: emptyList()
)

internal fun NetworkRoute.toExternalAs() = toLocal().toExternalAs()

internal fun List<Route>.toLocal() = map(Route::toLocal)

@JvmName("localToExternal")
internal fun List<RouteEntity>.toExternal() = map(RouteEntity::toExternalAs)

@JvmName("networkToLocal")
internal fun List<NetworkRoute>.toLocal() = map(NetworkRoute::toLocal)

@JvmName("networkToExternal")
internal fun List<NetworkRoute>.toExternalAs() = map(NetworkRoute::toExternalAs)