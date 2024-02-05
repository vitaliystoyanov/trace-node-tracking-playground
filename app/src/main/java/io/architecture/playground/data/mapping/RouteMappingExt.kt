package io.architecture.playground.data.mapping

import io.architecture.playground.data.local.model.LocalCoordinate
import io.architecture.playground.data.local.model.LocalRoute
import io.architecture.playground.data.remote.model.NetworkRoute
import io.architecture.playground.model.Coordinate
import io.architecture.playground.model.Route

fun Route.toLocal() = LocalRoute(
    nodeId = nodeId,
    route = route.map { LocalCoordinate(it.lat, it.lon) }
)

fun LocalRoute.toExternal() = Route(
    nodeId = nodeId,
    route = route?.map { Coordinate(it.lat, it.lon) } ?: emptyList()
)

fun NetworkRoute.toLocal() = LocalRoute(
    nodeId = nodeId,
    route = route?.map { coordinate ->
        LocalCoordinate(coordinate[0], coordinate[1])
    } ?: emptyList()
)

fun NetworkRoute.toExternal() = toLocal().toExternal()

fun List<Route>.toLocal() = map(Route::toLocal)

@JvmName("localToExternal")
fun List<LocalRoute>.toExternal() = map(LocalRoute::toExternal)

@JvmName("networkToLocal")
fun List<NetworkRoute>.toLocal() = map(NetworkRoute::toLocal)

@JvmName("networkToExternal")
fun List<NetworkRoute>.toExternal() = map(NetworkRoute::toExternal)