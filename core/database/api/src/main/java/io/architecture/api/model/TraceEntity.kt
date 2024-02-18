package io.architecture.api.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "traces"
)
data class TraceEntity(
    @ColumnInfo(name = "node_id")
    @PrimaryKey
    var nodeId: String,
    var lon: Double,
    var lat: Double,
    var speed: Int,
    var azimuth: Double,
    var alt: Double,
    var sentAtTime: Date
)