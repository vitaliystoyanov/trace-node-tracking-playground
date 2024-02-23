package io.architecture.database.imp.room.entity

import io.architecture.database.api.model.CoordinateEntity

internal data class RoomCoordinateEntity(override val lat: Double, override val lon: Double) :
    CoordinateEntity(lat, lon)