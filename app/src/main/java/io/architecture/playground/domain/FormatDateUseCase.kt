package io.architecture.playground.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FormatDateUseCase @Inject constructor() {

    operator fun invoke(date: Date, preferredPattern: String = DEFAULT_DATETIME_FORMAT): String =
        SimpleDateFormat(preferredPattern, Locale.getDefault()).format(date)

    companion object {
        const val DEFAULT_DATETIME_FORMAT: String = "yyyy-MM-dd HH:mm:ss:SSSSSSS"
    }
}