package io.architecture.domain

class ConvertAzimuthToDirectionUseCase { // TODO Support locale from user data repository

    operator fun invoke(azimuth: Double) = when (azimuth) {
        in 0.0..22.5 -> "N"
        in 22.5..45.0 -> "NNE"
        in 45.0..67.5 -> "ENE"
        in 67.5..90.0 -> "E"
        in 90.0..112.5 -> "ESE"
        in 112.5..135.0 -> "SE"
        in 135.0..157.5 -> "SSE"
        in 157.5..180.0 -> "S"

        in 180.0..202.5 -> "SSW"
        in 202.5..225.0 -> "SW"
        in 225.0..247.5 -> "WSW"
        in 247.5..270.0 -> "W"
        in 270.0..292.5 -> "WNW"
        in 292.5..315.0 -> "NW"
        in 315.0..337.5 -> "NNW"
        in 337.5..360.0 -> "N"

        else -> {
            "?"
        }
    }
}