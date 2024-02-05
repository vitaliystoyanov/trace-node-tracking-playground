package io.architecture.playground.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class LocalNodeWithRoute(
    @Embedded val node: LocalNode,
    @Relation(
        parentColumn = "node_id",
        entityColumn = "node_id"
    )
    val route: LocalRoute
)