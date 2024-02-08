package io.architecture.playground.data.local.model

import androidx.room.Embedded

data class NodeWithLastTraceEntity(

    @Embedded
    val node: NodeEntity,
    @Embedded
    val trace: TraceEntity
)