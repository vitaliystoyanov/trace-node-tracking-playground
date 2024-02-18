package io.architecture.playground.service

import io.architecture.playground.model.NetworkTrace
import io.architecture.playground.model.SampleNode
import io.architecture.playground.model.toDto
import io.architecture.playground.modules.LOGGER
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.Position
import io.github.dellisd.spatialk.turf.ExperimentalTurfApi
import io.github.dellisd.spatialk.turf.Units
import io.github.dellisd.spatialk.turf.along
import io.github.dellisd.spatialk.turf.bearing
import io.github.dellisd.spatialk.turf.bearingToAzimuth
import io.github.dellisd.spatialk.turf.length
import kotlinx.coroutines.delay
import java.lang.Math.random
import java.util.UUID
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

const val NODE_SAMPLE_NUM = 1_000
const val DEFAULT_DELAY = 1_000L

const val SPEED_RANDOM_MIN = 10
const val SPEED_RANDOM_MAX = 50

private fun randomIntFromInterval(min: Int, max: Int): Int = Random.nextInt(min, max + 1)
val sampleNodes = mutableMapOf<String, SampleNode>()

fun deleteNodes() = sampleNodes.clear()

@OptIn(ExperimentalTurfApi::class)
fun generateSamples() {
    if (sampleNodes.size == NODE_SAMPLE_NUM) return
    val lineStrings = mutableListOf<LineString>()
    repeat(NODE_SAMPLE_NUM) {
        lineStrings.add(
            LineString(List(5) {
                generateRandomLocationInsideRadius(37.88404, 42.11154, 3_000_00)
            })
        )
    }

    for (lineString in lineStrings) {
        val speed = randomIntFromInterval(SPEED_RANDOM_MIN, SPEED_RANDOM_MAX)
        val bearing =
            bearing(lineString.coordinates[0], lineString.coordinates[1])
        val azimuth = bearingToAzimuth(bearing)
        val nodeId = UUID.randomUUID().toString().split("-")[0]
        val steps = (length(lineString, Units.Kilometers) * 1000 / speed).roundToInt()

        sampleNodes[nodeId] = SampleNode(
            route = lineString,
            nodeId = nodeId,
            lon = lineString.coordinates[0].longitude,
            lat = lineString.coordinates[0].latitude,
            speed = speed,
            steps = steps,
            countStep = 0,
            azimuth = azimuth,
            mode = 1
        )
    }
}

// https://gist.github.com/EmmanuelGuther/a1698096fbf9bcf56e4847b36d517893
private fun generateRandomLocationInsideRadius(x0: Double, y0: Double, radius: Int): Position {
    val random = random()
    // Convert radius from meters to degrees
    val radiusInDegrees = (radius / 111000f).toDouble()
    val u = Math.random()
    val v = Math.random()
    val w = radiusInDegrees * sqrt(u)
    val t = 2.0 * PI * v
    val x = w * cos(t)
    val y = w * sin(t)
    // Adjust the x-coordinate for the shrinking of the east-west distances
    val newX = x / cos(Math.toRadians(y0))
    val foundLongitude = newX + x0
    val foundLatitude = y + y0
    return Position(doubleArrayOf(foundLongitude, foundLatitude))
}

@OptIn(ExperimentalTurfApi::class)
fun moveNode(node: SampleNode): Position = along(
    node.route,
    node.countStep * 0.001 * node.speed,
    units = Units.Kilometers
)

suspend inline fun sendTraces(block: (NetworkTrace) -> Unit) {
    generateSamples()
    var allSteps = sampleNodes.values.sumOf { it.steps }
    LOGGER.info("Prepared steps: $allSteps for ${sampleNodes.size} node(s)...")

    while (allSteps != 0) {
        sampleNodes.values.forEach {
            with(it) {
                if (countStep != steps) {
                    val newCords = moveNode(it)
                    countStep += 1
                    lon = newCords.longitude
                    lat = newCords.latitude
                    sampleNodes[nodeId] = it
                    block(it.toDto())
                }
            }
            allSteps -= 1
        }

        delay(DEFAULT_DELAY)
    }
}




