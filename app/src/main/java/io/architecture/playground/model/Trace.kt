package io.architecture.playground.model

data class Trace(

    val id: Long = 0,
    val lon: Double = 0.0,
    val lat: Double = 0.0,
    val speed: Int = 0,
    val bearing: Double = 0.0,
    val alt: Double = 0.0,
    val time: Long = 0,
    val nodeId: String = "",
    val mode: Int = 0

)