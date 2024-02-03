package io.architecture.playground.util


fun bearingAzimuthToDirection(bearing: Double):String {
    return if( bearing > 0 && bearing < 45 ) "S"
    else if( bearing >= 45 && bearing < 90 ) "SW"
    else if( bearing > 0 && bearing < 135 ) "W"
    else if( bearing > 0 && bearing < 180 ) "NW"
    else if( bearing > 0 && bearing < 225 ) "N"
    else if( bearing > 0 && bearing < 270 ) "NE"
    else if( bearing > 0 && bearing < 315 ) "E"
    else if( bearing > 0 && bearing < 360 ) "SE"
    else "?"
}