package io.architecture.playground.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "traces"
)
data class TraceEntity(
    @PrimaryKey
    @ColumnInfo(name = "node_id")
    val nodeId: String,
    val lon: Double,
    val lat: Double,
    val speed: Int,
    val azimuth: Double,
    val alt: Double,
    val time: Date
)