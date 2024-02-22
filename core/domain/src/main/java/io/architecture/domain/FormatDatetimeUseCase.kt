package io.architecture.domain

import org.koin.core.annotation.Single
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormatDatetimeUseCase() {

    operator fun invoke(date: Date, preferredPattern: String = DEFAULT_DATETIME_FORMAT): String =
        SimpleDateFormat(
            preferredPattern,
            Locale.getDefault()
        ).format(date) // TODO Better to have user data repository for local and other parameters

    companion object {
        const val DEFAULT_DATETIME_FORMAT: String = "yyyy-MM-dd HH:mm:ss:SSSSSSS"
    }
}