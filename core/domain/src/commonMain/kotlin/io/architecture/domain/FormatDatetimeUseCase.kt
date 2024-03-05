package io.architecture.domain

import kotlinx.datetime.LocalDateTime

class FormatDatetimeUseCase {

    operator fun invoke(
        date: LocalDateTime
    ): String {
        // TODO Better to have user data repository for local and other parameters
        // If you target Android devices running below API 26, you need to use Android Gradle plugin 4.0
        // or newer and enable core library desugaring.
        // https://github.com/Kotlin/kotlinx-datetime?tab=readme-ov-file#using-in-your-projects
        return date.toString()
    }
}