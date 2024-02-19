package io.architecture.data.mapping

import io.architecture.database.api.model.NodeEntity
import io.architecture.model.Node
import io.architecture.model.NodeMode

internal fun Node.toLocal() = NodeEntity(
    id = id,
    mode = mode.value
)

internal fun NodeEntity.toExternalAs() = Node(
    id = id,
    mode = NodeMode.valueOf(mode)
)

internal fun List<Node>.toLocal() = map(Node::toLocal)

@JvmName("localToExternal")
internal fun List<NodeEntity>.toExternalAs() = map(NodeEntity::toExternalAs)


internal fun assignProperties(nodePooled: NodeEntity, source: Node): NodeEntity = nodePooled.apply {
    id = source.id
    mode = source.mode.value
}


internal fun assignProperties(nodePooled: Node, source: NodeEntity): Node = nodePooled.apply {
    id = source.id
    mode = NodeMode.valueOf(source.mode)
}