package io.architecture.playground.data.local.convertors

import androidx.room.TypeConverter
import io.architecture.playground.data.local.model.CoordinateEntity

class ListCoordinatesTypeConvertor {
    @TypeConverter
    fun fromArrayListOfCoordinate(list: List<CoordinateEntity>?): String =
        list
            ?.map { it.lat.toString() + "," + it.lon.toString() }
            ?.joinToString(separator = ";") { it } ?: ""

    @TypeConverter
    fun toArrayListOfCoordinate(string: String): List<CoordinateEntity>? =
        string
            .split(";")
            .map {
                CoordinateEntity(
                    it.split(",")[1].toDouble(),
                    it.split(",")[0].toDouble()
                )
            }
            .toList().also { ArrayList(it) }
}