package io.architecture.playground.data.mapping

import io.architecture.playground.data.local.model.NodeEntity
import io.architecture.playground.model.Node
import io.architecture.playground.model.NodeMode

fun Node.toLocal() = NodeEntity(
    id = id,
    mode = mode.value
)

fun NodeEntity.toExternal() = Node(
    id = id,
    mode = NodeMode.valueOf(mode)
)

fun List<Node>.toLocal() = map(Node::toLocal)

@JvmName("localToExternal")
fun List<NodeEntity>.toExternal() = map(NodeEntity::toExternal)


fun assignProperties(nodePooled: NodeEntity, source: Node): NodeEntity = nodePooled.apply {
    id = source.id
    mode = source.mode.value
}


fun assignProperties(nodePooled: Node, source: NodeEntity): Node = nodePooled.apply {
    id = source.id
    mode = NodeMode.valueOf(source.mode)
}