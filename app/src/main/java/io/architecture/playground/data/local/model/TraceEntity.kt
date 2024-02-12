package io.architecture.playground.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.architecture.playground.core.pool.PoolMember
import java.util.Date

@Entity(
    tableName = "traces"
)
data class TraceEntity(
    @ColumnInfo(name = "trace_id")
    var id: Long,
    @ColumnInfo(name = "node_id")
    @PrimaryKey
    var nodeId: String,
    var lon: Double,
    var lat: Double,
    var speed: Int,
    var azimuth: Double,
    var alt: Double,
    var sentAtTime: Date
) : PoolMember {

    override fun finalize() = run {
        id = 0
        nodeId = ""
        lon = 0.0
        lat = 0.0
        speed = 0
        azimuth = 0.0
        alt = 0.0
        sentAtTime = Date(0)
    }
}