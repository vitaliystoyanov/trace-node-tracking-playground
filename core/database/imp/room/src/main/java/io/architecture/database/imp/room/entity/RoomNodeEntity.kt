package io.architecture.database.imp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.architecture.database.api.model.NodeEntity


@Entity(
    tableName = "nodes"
)
internal class RoomNodeEntity(
    @PrimaryKey override var id: String,
    override var mode: Int,
) : NodeEntity(id, mode) {

    companion object {
        fun create(abstract: NodeEntity): RoomNodeEntity {
            return RoomNodeEntity(abstract.id, abstract.mode)
        }
    }
}