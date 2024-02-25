package io.architecture.model

import kotlinx.datetime.LocalDateTime

data class Trace(
    var nodeId: String = "",
    var lon: Double = 0.0,
    var lat: Double = 0.0,
    var speed: Int = 0,
    var azimuth: Double = 0.0,
    var alt: Double = 0.0,
    var sentAtTime: LocalDateTime,

    var formattedDatetime: String? = "",
    var direction: String? = "",
)

