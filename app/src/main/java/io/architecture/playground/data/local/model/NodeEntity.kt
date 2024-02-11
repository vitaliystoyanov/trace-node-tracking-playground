package io.architecture.playground.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.architecture.playground.core.pool.PoolMember
import io.architecture.playground.model.NodeMode

@Entity(
    tableName = "nodes"
)
data class NodeEntity(
    @PrimaryKey var id: String,
    var mode: Int,
    var lastTraceTimestamp: Long,
) : PoolMember {

    override fun finalize() = run {
        id = ""
        mode = NodeMode.UNKNOWN.value
        lastTraceTimestamp = -1
    }
}