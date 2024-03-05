package io.architecture.database.api.model

import io.architecture.model.Node
import io.architecture.model.NodeMode
import kotlin.jvm.JvmName


open class NodeEntity(
    open var id: String,
    open var mode: Int,
) {
    override fun toString(): String {
        return "NodeEntity(id='$id', mode=$mode)"
    }
}

fun <T : NodeEntity> T.toExternal(): Node = Node(id = id, mode = NodeMode.valueOf(mode))

@JvmName("localToExternal")
fun <T : NodeEntity> List<T>.toExternal(): List<Node> = map { it.toExternal() }

fun Node.toLocal(): NodeEntity = NodeEntity(id = id, mode = mode.value)

@JvmName("externalToLocal")
fun List<Node>.toLocal(): List<NodeEntity> = map { it.toLocal() }


