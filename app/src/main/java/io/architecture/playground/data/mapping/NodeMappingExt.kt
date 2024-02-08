package io.architecture.playground.data.mapping

import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.NodeWithLastTraceEntity
import io.architecture.playground.model.Node
import io.architecture.playground.model.NodeMode
import io.architecture.playground.model.Trace

fun Node.toLocal() = NodeEntity(
    id = id,
    mode = mode.value,
    lastTraceTimestamp = lastTraceTimestamp
)

fun NodeEntity.toExternal() = Node(
    id = id,
    mode = NodeMode.valueOf(mode),
    lastTraceTimestamp = lastTraceTimestamp
)

fun NodeWithLastTraceEntity.toExternalPair(): Pair<Node, Trace> = Pair(
    this.node.toExternal(),
    this.trace.toExternal()
)

//fun NetworkRoute.toLocal() = RouteEntity(
//    nodeId = nodeId,
//    route = route?.map { coordinate ->
//        CoordinateEntity(coordinate[0], coordinate[1])
//    } ?: emptyList()
//)
//
//fun NetworkRoute.toExternal() = toLocal().toExternal()

fun List<Node>.toLocal() = map(Node::toLocal)

@JvmName("localToExternal")
fun List<NodeEntity>.toExternal() = map(NodeEntity::toExternal)

@JvmName("localToExternal")
fun List<NodeWithLastTraceEntity>.toExternalPairs() = map(NodeWithLastTraceEntity::toExternalPair).toSet()

//@JvmName("networkToLocal")
//fun List<NetworkRoute>.toLocal() = map(NetworkRoute::toLocal)
//
//@JvmName("networkToExternal")
//fun List<NetworkRoute>.toExternal() = map(NetworkRoute::toExternal)