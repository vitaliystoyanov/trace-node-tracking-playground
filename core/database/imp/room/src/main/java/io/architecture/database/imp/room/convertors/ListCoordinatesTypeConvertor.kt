package io.architecture.database.imp.room.convertors

import androidx.room.TypeConverter
import io.architecture.api.model.CoordinateEntity

private const val COORDINATE_DELIMITER = ","
private const val COORDINATE_TERMINAL = ";"

class ListCoordinatesTypeConvertor {
    @TypeConverter
    fun fromArrayListOfCoordinate(list: List<CoordinateEntity>?): String =
        list
            ?.asSequence()
            ?.map { it.lat.toString() + COORDINATE_DELIMITER + it.lon.toString() }
            ?.joinToString(COORDINATE_TERMINAL) { it } ?: ""

    @TypeConverter
    fun toArrayListOfCoordinate(string: String): List<CoordinateEntity>? =
        string
            .split(";")
            .asSequence()
            .map {
                CoordinateEntity(
                    it.split(COORDINATE_DELIMITER)[1].toDouble(),
                    it.split(COORDINATE_DELIMITER)[0].toDouble()
                )
            }
            .toList()
}