package io.architecture.playground.service

import io.architecture.playground.model.NetworkTrace
import io.github.dellisd.spatialk.geojson.LineString
import io.github.dellisd.spatialk.geojson.Position
import io.github.dellisd.spatialk.turf.ExperimentalTurfApi
import io.github.dellisd.spatialk.turf.Units
import io.github.dellisd.spatialk.turf.along
import io.github.dellisd.spatialk.turf.bearing
import io.github.dellisd.spatialk.turf.bearingToAzimuth
import io.github.dellisd.spatialk.turf.length
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.random.Random

const val NODE_SAMPLE_NUM = 10
const val DEFAULT_DELAY = 1000L
val BLACK_SEA_BBOX = arrayOf(29.007088, 44.084696, 37.88404, 42.11154)
val point1 = Position(doubleArrayOf(29.007088, 44.084696))
val point2 = Position(doubleArrayOf(37.88404, 42.11154))
const val SPEED_RANDOM_MIN = 10
const val SPEED_RANDOM_MAX = 50


fun randomIntFromInterval(min: Int, max: Int): Int = Random.nextInt(min, max + 1)

val sampleNodes = mutableListOf<SampleNode>()
fun calcSteps() = sampleNodes.sumOf { it.steps }

@OptIn(ExperimentalTurfApi::class)
fun generateSamples() {
//    val lineStrings = turf.randomLineString(
//        NODE_SAMPLE_NUM,
//        bbox = BLACK_SEA_BBOX,
//        maxLength = 1.0,
//        numVertices = 2
//    )
    val lineStrings = mutableListOf<LineString>()
    repeat(NODE_SAMPLE_NUM) { lineStrings.add(LineString(point1, point2)) }

    for (lineString in lineStrings) {
        val speed = randomIntFromInterval(SPEED_RANDOM_MIN, SPEED_RANDOM_MAX)
        val bearing =
            bearing(lineString.coordinates[0], lineString.coordinates[1])
        val azimuth = bearingToAzimuth(bearing)
        val nodeId = UUID.randomUUID().toString().split("-")[0]
        val steps = (length(lineString, Units.Kilometers) * 1000 / speed).roundToInt()

        sampleNodes.add(
            SampleNode(
                route = lineString,
                nodeId = nodeId,
                speed = speed,
                steps = steps,
                countStep = 0,
                azimuth = azimuth,
                mode = 1
            )
        )
    }
}

suspend inline fun sendTraces(block: (NetworkTrace) -> Unit) {

    generateSamples()

    var allSteps = calcSteps()
    println("Prepared steps: $allSteps for ${sampleNodes.size} node(s)")

    while (allSteps != 0) {
        sampleNodes.forEach { node ->
            if (node.countStep != node.steps) {
                val newCords = moveNode(node)
                block(node.toDto(newCords))
            }
            allSteps -= 1
        }

        delay(DEFAULT_DELAY)
    }
}

data class SampleNode(
    val route: LineString,
    val nodeId: String,
    val speed: Int,
    var steps: Int,
    var countStep: Int,
    val azimuth: Double,
    var mode: Int,
    var time: Long = 0L
)

fun SampleNode.toDto(position: Position) = NetworkTrace(
    type = "trace",
    lon = position.longitude,
    lat = position.latitude,
    nodeId = nodeId,
    speed = speed,
    azimuth = azimuth,
    mode = mode,
    sentAtTime = System.currentTimeMillis()
)

@OptIn(ExperimentalTurfApi::class)
fun moveNode(node: SampleNode): Position = along(
    node.route,
    node.countStep * 0.001 * node.speed,
    units = Units.Kilometers
)




