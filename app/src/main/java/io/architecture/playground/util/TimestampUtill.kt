package io.architecture.playground.util

import android.annotation.SuppressLint
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

const val DEFAULT_DATETIME_FORMAT: String = "yyyy-MM-dd HH:mm:ss:SSSSSSS"

@SuppressLint("SimpleDateFormat")
fun toDatetime(timestamp: Long, formatPattern: String = DEFAULT_DATETIME_FORMAT): String =
    SimpleDateFormat(formatPattern).format(Date(Timestamp(timestamp).time))
