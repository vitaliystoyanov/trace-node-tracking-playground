package io.architecture.playground.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "nodes"
)
data class LocalNode(
    @PrimaryKey val nodeId: String,
    val lon: Double,
    val lat: Double,
    val speed: Int,
    val azimuth: Double,
    val alt: Double,
    val time: Long,
    val mode: Int
)