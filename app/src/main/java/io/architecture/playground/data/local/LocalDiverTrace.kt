package io.architecture.playground.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "diver_traces"
)
data class LocalDiverTrace(
    @PrimaryKey val id: String,
    val ept: Double,
    val eps: Double,
    val epv: Int,
    val lon: Double,
    val time: String,
    val epd: Double,
    val epx: Int,
    val speed: Double,
    val alt: Int,
    val epy: Int,
    val track: Double,
    val lat: Double,
    val mode: Int
)