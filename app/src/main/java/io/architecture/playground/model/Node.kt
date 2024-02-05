package io.architecture.playground.model

import java.util.Date

data class Node(
    val nodeId: String = "",
    val id: Long = 0,
    val lon: Double = 0.0,
    val lat: Double = 0.0,
    val speed: Int = 0,
    val azimuth: Double = 0.0,
    val alt: Double = 0.0,
    val time: Date,
    val mode: NodeMode = NodeMode.INACTIVE,

    var formattedDatetime: String? = "",
    var direction: String? = ""
)

enum class NodeMode(var valueInt: Int) {
    ACTIVE(1),
    INACTIVE(0)
}