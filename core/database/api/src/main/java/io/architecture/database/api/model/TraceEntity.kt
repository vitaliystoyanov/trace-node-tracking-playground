package io.architecture.database.api.model

import io.architecture.model.Trace
import java.util.Date

open class TraceEntity(
    open var nodeId: String,
    open var lon: Double,
    open var lat: Double,
    open var speed: Int,
    open var azimuth: Double,
    open var alt: Double,
    open var sentAtTime: Date,
)

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