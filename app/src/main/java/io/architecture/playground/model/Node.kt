package io.architecture.playground.model

data class Node(

    val nodeId: String = "",
    val id: Long = 0,
    val lon: Double = 0.0,
    val lat: Double = 0.0,
    val speed: Int = 0,
    val azimuth: Double = 0.0,
    val alt: Double = 0.0,
    val time: Long = 0,
    val mode: NodeMode = NodeMode.INACTIVE

)

enum class NodeMode(var valueInt: Int) {
    ACTIVE(1),
    INACTIVE(0)
}