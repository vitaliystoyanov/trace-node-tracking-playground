package io.architecture.database.imp.room.convertors

import androidx.room.TypeConverter
import java.util.Date


internal class DateTypeConvertor {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

}