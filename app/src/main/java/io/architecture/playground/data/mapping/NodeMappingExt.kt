package io.architecture.playground.data.mapping

import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.data.local.model.NodeWithLastTraceEntity
import io.architecture.playground.model.CompositeNodeTrace
import io.architecture.playground.model.Node
import io.architecture.playground.model.NodeMode

fun Node.toLocal() = NodeEntity(
    id = id,
    mode = mode.value,
    lastTraceTimestamp = sentTime
)

fun NodeEntity.toExternal() = Node(
    id = id,
    mode = NodeMode.valueOf(mode),
    sentTime = lastTraceTimestamp
)

fun NodeWithLastTraceEntity.toExternal(): CompositeNodeTrace = CompositeNodeTrace(
    node.toExternal(),
    trace.toExternal()
)


fun List<Node>.toLocal() = map(Node::toLocal)

@JvmName("localToExternal")
fun List<NodeEntity>.toExternal() = map(NodeEntity::toExternal)

@JvmName("localToExternal")
fun List<NodeWithLastTraceEntity>.toExternal() =
    map(NodeWithLastTraceEntity::toExternal).asSequence()


fun assignProperties(nodePooled: NodeEntity, source: Node): NodeEntity = nodePooled.apply {
    id = source.id
    mode = source.mode.value
}


fun assignProperties(nodePooled: Node, source: NodeEntity): Node = nodePooled.apply {
    id = source.id
    mode = NodeMode.valueOf(source.mode)
    sentTime = source.lastTraceTimestamp
}