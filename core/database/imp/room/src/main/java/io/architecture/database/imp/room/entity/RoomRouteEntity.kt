package io.architecture.database.imp.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.architecture.database.api.model.CoordinateEntity
import io.architecture.database.api.model.RouteEntity

@Entity(
    tableName = "routes"
)
internal data class RoomRouteEntity(
    @PrimaryKey
    @ColumnInfo(name = "node_id")
    override var nodeId: String,
    override var route: List<CoordinateEntity>?,
) : RouteEntity(nodeId, route) {

    companion object {
        fun create(abstract: RouteEntity): RoomRouteEntity {
            return RoomRouteEntity(abstract.nodeId, abstract.route)
        }
    }
}