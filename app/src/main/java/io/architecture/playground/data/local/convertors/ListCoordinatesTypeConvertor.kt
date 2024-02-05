package io.architecture.playground.data.local.convertors

import android.util.Log
import androidx.room.TypeConverter
import io.architecture.playground.data.local.model.LocalCoordinate

class ListCoordinatesTypeConvertor {
    @TypeConverter
    fun fromArrayListOfLocalCoordinate(list: List<LocalCoordinate>?): String =
        list
            ?.map { it.lat.toString() + "," + it.lon.toString() }
            ?.joinToString(separator = ";") { it } ?: ""

    @TypeConverter
    fun toArrayListOfLocalCoordinate(string: String): List<LocalCoordinate>? =
        string
            .split(";")
            .map {
                LocalCoordinate(
                    it.split(",")[1].toDouble(),
                    it.split(",")[0].toDouble()
                )
            }
            .toList().also { ArrayList(it) }
}