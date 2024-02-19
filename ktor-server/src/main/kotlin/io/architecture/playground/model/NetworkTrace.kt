package io.architecture.playground.model

import kotlinx.serialization.Serializable

// Example of payload
//    "l": 30.52323011454044,
//    "lt": 42.725705657669245,
//    "s": 13,
//    "az": 3.4595129834094243,
//    "a": 0.1,
//    "t": 1706894164046,
//    "n": "bb0c326c-c1ee-11ee-b86a-d106324846e3",
//    "m": 1

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
    var mode: Int = 0
)