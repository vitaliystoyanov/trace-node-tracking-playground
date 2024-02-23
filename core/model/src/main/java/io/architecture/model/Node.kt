package io.architecture.model

data class Node(
    var id: String,
    var mode: NodeMode
) {
    constructor(id: String, mode: Int) : this(
        id,
        NodeMode.valueOf(mode)
    )
}

enum class NodeMode(var value: Int) {
    ACTIVE(1),
    INACTIVE(0),
    UNKNOWN(-1);

    companion object {
        fun valueOf(mode: Int) = NodeMode.entries.firstOrNull { it.value == mode } ?: UNKNOWN
    }
}

inline fun <reified T : Enum<T>> printAllValues() {
    println(enumValues<T>().joinToString { it.name })
}
