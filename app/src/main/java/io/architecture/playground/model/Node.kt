package io.architecture.playground.model

data class Node(
    val id: String,
    val mode: NodeMode,
    val lastTraceTimestamp: Long,
) {
    constructor(id: String, mode: Int, lastTraceTimestamp: Long) : this(
        id,
        NodeMode.valueOf(mode),
        lastTraceTimestamp
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
