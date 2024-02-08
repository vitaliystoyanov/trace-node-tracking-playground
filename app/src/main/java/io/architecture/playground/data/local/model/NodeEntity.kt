package io.architecture.playground.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "nodes"
)
data class NodeEntity(
    @PrimaryKey val id: String,
    val mode: Int,
    val lastTraceTimestamp: Long,
)