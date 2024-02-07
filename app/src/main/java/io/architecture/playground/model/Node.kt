package io.architecture.playground.model

import java.util.Date

data class Node(
    var nodeId: String = "",
    var id: Long = 0,
    var lon: Double = 0.0,
    var lat: Double = 0.0,
    var speed: Int = 0,
    var azimuth: Double = 0.0,
    var alt: Double = 0.0,
    var time: Date,
    var mode: NodeMode = NodeMode.INACTIVE,

    var formattedDatetime: String? = "",
    var direction: String? = ""
)

enum class NodeMode(var valueInt: Int) {
    ACTIVE(1),
    INACTIVE(0)
}