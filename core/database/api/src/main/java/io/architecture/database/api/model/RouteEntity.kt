package io.architecture.database.api.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.architecture.database.api.model.CoordinateEntity

@Entity(
    tableName = "routes"
)
class RouteEntity(
    @PrimaryKey
    @ColumnInfo(name = "node_id")
    val nodeId: String,
    val route: List<CoordinateEntity>?
)