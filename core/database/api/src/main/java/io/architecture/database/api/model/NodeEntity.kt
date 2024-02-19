package io.architecture.database.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "nodes"
)
data class NodeEntity(
    @PrimaryKey var id: String,
    var mode: Int,
)