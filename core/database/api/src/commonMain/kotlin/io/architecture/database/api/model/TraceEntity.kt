package io.architecture.database.api.model

import io.architecture.model.Trace
import kotlinx.datetime.LocalDateTime
import kotlin.jvm.JvmName

open class TraceEntity(
    open var nodeId: String,
    open var lon: Double,
    open var lat: Double,
    open var speed: Int,
    open var azimuth: Double,
    open var alt: Double,
    open var sentAtTime: LocalDateTime,
) {
    override fun toString(): String {
        return "TraceEntity(nodeId='$nodeId', lon=$lon, lat=$lat, speed=$speed, azimuth=$azimuth, alt=$alt, sentAtTime=$sentAtTime)"
    }
}

fun <T : TraceEntity> T.toExternal(): Trace = Trace(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = sentAtTime,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

fun Trace.toLocal(): TraceEntity = TraceEntity(
    nodeId = nodeId,
    lon = lon,
    sentAtTime = sentAtTime,
    speed = speed,
    azimuth = azimuth,
    alt = alt,
    lat = lat,
)

@JvmName("externalToLocal")
fun List<Trace>.toLocal(): List<TraceEntity> = map { it.toLocal() }

@JvmName("localToExternal")
fun <T : TraceEntity> List<T>.toExternal(): List<Trace> = map { it.toExternal() }