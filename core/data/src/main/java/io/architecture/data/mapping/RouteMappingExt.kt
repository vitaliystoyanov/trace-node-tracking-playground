package io.architecture.data.mapping

import io.architecture.database.api.model.CoordinateEntity
import io.architecture.api.model.NetworkRoute
import io.architecture.database.api.model.RouteEntity
import io.architecture.model.Coordinate
import io.architecture.model.Route

fun Route.toLocal() = RouteEntity(
    nodeId = nodeId,
    route = coordinates.map { CoordinateEntity(it.lat, it.lon) }
)

fun RouteEntity.toExternalAs() = Route(
    nodeId = nodeId,
    coordinates = route?.map { Coordinate(it.lat, it.lon) } ?: emptyList()
)

fun NetworkRoute.toLocal() = RouteEntity(
    nodeId = nodeId,
    route = route?.map { coordinate ->
        CoordinateEntity(coordinate[0], coordinate[1])
    } ?: emptyList()
)

fun NetworkRoute.toExternalAs() = toLocal().toExternalAs()

fun List<Route>.toLocal() = map(Route::toLocal)

@JvmName("localToExternal")
fun List<RouteEntity>.toExternal() = map(RouteEntity::toExternalAs)

@JvmName("networkToLocal")
fun List<NetworkRoute>.toLocal() = map(NetworkRoute::toLocal)

@JvmName("networkToExternal")
fun List<NetworkRoute>.toExternalAs() = map(NetworkRoute::toExternalAs)