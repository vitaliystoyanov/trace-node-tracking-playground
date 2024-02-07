package io.architecture.playground.data.local.model

import androidx.room.Embedded
import androidx.room.Relation

data class NodeWithRouteEntity(
    @Embedded val node: NodeEntity,
    @Relation(
        parentColumn = "node_id",
        entityColumn = "node_id"
    )
    val route: RouteEntity
)