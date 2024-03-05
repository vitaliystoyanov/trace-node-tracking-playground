package io.architecture.database.api.model

import io.architecture.model.Coordinate
import io.architecture.model.Route
import kotlin.jvm.JvmName


open class RouteEntity(
    open var nodeId: String,
    open var route: List<CoordinateEntity>?,
) {
    override fun toString(): String {
        return "RouteEntity(nodeId='$nodeId', route=$route)"
    }
}

fun <T : RouteEntity> T.toExternal() = Route(
    nodeId = nodeId,
    coordinates = route?.map { Coordinate(it.lat, it.lon) } ?: emptyList()
)

@JvmName("localToExternal")
fun <T : RouteEntity> List<T>.toExternal(): List<Route> = map { it.toExternal() }

fun Route.toLocal(): RouteEntity = RouteEntity(
    nodeId = nodeId,
    route = coordinates.map { CoordinateEntity(it.lat, it.lon) }
)

@JvmName("externalToLocal")
fun List<Route>.toLocal(): List<RouteEntity> = map { it.toLocal() }