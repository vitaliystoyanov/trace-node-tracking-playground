package io.architecture.playground.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "traces"
)
data class LocalTrace(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val lon: Double,
    val lat: Double,
    val speed: Int,
    val azimuth: Double,
    val alt: Double,
    val time: Long,
    val nodeId: String,
    val mode: Int
)