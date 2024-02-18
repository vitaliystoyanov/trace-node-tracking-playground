package io.architecture.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FormatDatetimeUseCase @Inject constructor() {

    operator fun invoke(date: Date, preferredPattern: String = DEFAULT_DATETIME_FORMAT): String =
        SimpleDateFormat(
            preferredPattern,
            Locale.getDefault()
        ).format(date) // TODO Better to have user data repository for local and other parameters

    companion object {
        const val DEFAULT_DATETIME_FORMAT: String = "yyyy-MM-dd HH:mm:ss:SSSSSSS"
    }
}