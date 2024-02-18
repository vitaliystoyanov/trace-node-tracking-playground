package io.architecture.room.convertors

import androidx.room.TypeConverter
import java.util.Date


class DateTypeConvertor {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

}