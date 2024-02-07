package io.architecture.playground.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "node_routes"
)
class RouteEntity(
    @PrimaryKey
    @ColumnInfo(name = "node_id")
    val nodeId: String,
    val route: List<CoordinateEntity>?
)