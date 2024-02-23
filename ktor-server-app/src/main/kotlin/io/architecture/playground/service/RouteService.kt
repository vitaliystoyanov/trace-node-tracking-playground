package io.architecture.playground.service

import io.architecture.network.websocket.api.model.NetworkRoute
import io.architecture.playground.model.SampleNode
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

suspend inline fun sendRoutes(block: (NetworkRoute) -> Unit) {
    if (sampleNodes.isNotEmpty()) {
        sampleNodes.values.forEach {
            block(it.toRouteDto())
            delay(100.milliseconds)
        }
    }
}

fun SampleNode.toRouteDto() = NetworkRoute(
    type = "route",
    nodeId = nodeId,
    route = List(route.coordinates.size) { index ->
        doubleArrayOf(route.coordinates[index].longitude, route.coordinates[index].latitude)
    }.toTypedArray()

)