package io.architecture.playground.data.mapping

import io.architecture.playground.data.local.model.CoordinateEntity
import io.architecture.playground.data.local.model.RouteEntity
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.model.Coordinate
import io.architecture.playground.model.Route

fun Route.toLocal() = RouteEntity(
    nodeId = nodeId,
    route = coordinates.map { CoordinateEntity(it.lat, it.lon) }
)

fun RouteEntity.toExternal() = Route(
    nodeId = nodeId,
    coordinates = route?.map { Coordinate(it.lat, it.lon) } ?: emptyList()
)

fun NetworkRoute.toLocal() = RouteEntity(
    nodeId = nodeId,
    route = route?.map { coordinate ->
        CoordinateEntity(coordinate[0], coordinate[1])
    } ?: emptyList()
)

fun NetworkRoute.toExternal() = toLocal().toExternal()

fun List<Route>.toLocal() = map(Route::toLocal)

@JvmName("localToExternal")
fun List<RouteEntity>.toExternal() = map(RouteEntity::toExternal)

@JvmName("networkToLocal")
fun List<NetworkRoute>.toLocal() = map(NetworkRoute::toLocal)

@JvmName("networkToExternal")
fun List<NetworkRoute>.toExternal() = map(NetworkRoute::toExternal)