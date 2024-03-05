package io.architecture.database.api.model

import io.architecture.model.Coordinate
import kotlin.jvm.JvmName

open class CoordinateEntity(
    open val lat: Double,
    open val lon: Double,
)

fun <T : CoordinateEntity> T.toExternal(): Coordinate = Coordinate(lon = lon, lat = lat)

@JvmName("localToExternal")
fun <T : CoordinateEntity> List<T>.toExternal(): List<Coordinate> = map { it.toExternal() }

fun Coordinate.toLocal(): CoordinateEntity = CoordinateEntity(
    lon = lon,
    lat = lat
)

@JvmName("externalToLocal")
fun List<Coordinate>.toLocal(): List<CoordinateEntity> = map { it.toLocal() }