package io.architecture.data.mapping

import io.architecture.api.model.NodeEntity
import io.architecture.model.Node
import io.architecture.model.NodeMode

fun Node.toLocal() = NodeEntity(
    id = id,
    mode = mode.value
)

fun NodeEntity.toExternalAs() = Node(
    id = id,
    mode = NodeMode.valueOf(mode)
)

fun List<Node>.toLocal() = map(Node::toLocal)

@JvmName("localToExternal")
fun List<NodeEntity>.toExternalAs() = map(NodeEntity::toExternalAs)


fun assignProperties(nodePooled: NodeEntity, source: Node): NodeEntity = nodePooled.apply {
    id = source.id
    mode = source.mode.value
}


fun assignProperties(nodePooled: Node, source: NodeEntity): Node = nodePooled.apply {
    id = source.id
    mode = NodeMode.valueOf(source.mode)
}