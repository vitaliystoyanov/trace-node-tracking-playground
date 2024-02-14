package io.architecture.playground.service

import io.architecture.playground.model.NetworkRoute

suspend inline fun sendRoutes(block: (NetworkRoute) -> Unit) {
    if (sampleNodes.isNotEmpty()) {
        sampleNodes.forEach {
//            block(it.toRouteDto())
        }
    }
}


//fun SampleNode.toRouteDto(position: Position) = NetworkRoute(
//    type = "route",
//    nodeId = nodeId,
//    route = arrayOf(route.coordinates { doubleArrayOf(it.coordinates) })
//    )