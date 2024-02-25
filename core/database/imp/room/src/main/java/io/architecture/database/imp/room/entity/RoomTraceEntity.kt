package io.architecture.database.imp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.architecture.database.api.model.TraceEntity
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "traces"
)
internal data class RoomTraceEntity(
    @ColumnInfo(name = "node_id")
    @PrimaryKey
    override var nodeId: String,
    override var lon: Double,
    override var lat: Double,
    override var speed: Int,
    override var azimuth: Double,
    override var alt: Double,
    override var sentAtTime: LocalDateTime,
) : TraceEntity(nodeId, lon, lat, speed, azimuth, alt, sentAtTime) {

    companion object {
        fun create(abstract: TraceEntity): RoomTraceEntity {
            return RoomTraceEntity(
                nodeId = abstract.nodeId,
                lon = abstract.lon,
                sentAtTime = abstract.sentAtTime,
                speed = abstract.speed,
                azimuth = abstract.azimuth,
                alt = abstract.alt,
                lat = abstract.lat,
            )
        }
    }
}