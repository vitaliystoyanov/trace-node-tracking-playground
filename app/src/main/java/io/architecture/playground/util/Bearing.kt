package io.architecture.playground.util

fun bearingAzimuthToDirection(bearing: Double):String {

    return when (bearing) {
        in 0.0..22.5 -> return "N"
        in 22.5..45.0 -> return "NNE"
        in 45.0..67.5 -> return "ENE"
        in 67.5..90.0 -> return "E"
        in 90.0..112.5 -> return "ESE"
        in 112.5..135.0  -> return "SE"
        in 135.0..157.5  -> return "SSE"
        in 157.5 ..180.0  -> return "S"

        in 180.0..202.5 -> return "SSW"
        in 202.5..225.0 -> return "SW"
        in 225.0..247.5 -> return "WSW"
        in 247.5..270.0 -> return "W"
        in 270.0..292.5 -> return "WNW"
        in 292.5..315.0  -> return "NW"
        in 315.0 ..337.5  -> return "NNW"
        in 337.5 ..360.0  -> return "N"

        else -> { "?"}
    }


}