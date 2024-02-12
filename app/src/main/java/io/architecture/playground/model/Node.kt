package io.architecture.playground.model

import io.architecture.playground.core.pool.PoolMember

data class Node(
    var id: String,
    var mode: NodeMode,
    var sentTime: Long,
) : PoolMember {
    constructor(id: String, mode: Int, lastTraceTimestamp: Long) : this(
        id,
        NodeMode.valueOf(mode),
        lastTraceTimestamp
    )

    override fun finalize() = run {
        id = ""
        mode = NodeMode.UNKNOWN
        sentTime = -1
    }
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
