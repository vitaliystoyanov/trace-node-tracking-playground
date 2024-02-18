package io.architecture.playground.model

import io.github.dellisd.spatialk.geojson.LineString

data class SampleNode(
    val route: LineString,
    val nodeId: String,
    var lon: Double,
    var lat: Double,
    val speed: Int,
    var steps: Int,
    var countStep: Int,
    val azimuth: Double,
    var mode: Int,
    var time: Long = 0L
)

fun SampleNode.toDto() = NetworkTrace(
    type = "trace",
    lon = lon,
    lat = lat,
    nodeId = nodeId,
    speed = speed,
    azimuth = azimuth,
    mode = 1, // Always moving
    sentAtTime = System.currentTimeMillis()
)