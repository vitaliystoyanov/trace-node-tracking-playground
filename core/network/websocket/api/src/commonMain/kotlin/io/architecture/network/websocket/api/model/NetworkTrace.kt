package io.architecture.network.websocket.api.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTrace(
    var type: String = "",
    var lon: Double = 0.0,
    var lat: Double = 0.0,
    var speed: Int = 0,
    var azimuth: Double = 0.0,
    var alt: Double = 0.0,
    var sentAtTime: Long = 0,
    var nodeId: String = "",
    var mode: Int = 0,
)