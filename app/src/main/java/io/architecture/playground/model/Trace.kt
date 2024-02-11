package io.architecture.playground.model

import io.architecture.playground.core.pool.PoolMember
import java.util.Date

data class Trace(
    var id: Long = 0,
    var nodeId: String = "",
    var lon: Double = 0.0,
    var lat: Double = 0.0,
    var speed: Int = 0,
    var azimuth: Double = 0.0,
    var alt: Double = 0.0,
    var time: Date,

    var formattedDatetime: String? = "",
    var direction: String? = ""
) : PoolMember {


    override fun finalize() = run {
        id = 0
        nodeId = ""
        lon = 0.0
        lat = 0.0
        speed = 0
        azimuth = 0.0
        alt = 0.0
        time = Date()

        formattedDatetime = ""
        direction = ""
    }
}

