package io.architecture.model

data class CompositeNodeTrace(
    val node: Node,
    val trace: Trace
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        other as CompositeNodeTrace

        if (node != other.node) return false
        return trace == other.trace
    }

    override fun hashCode(): Int {
        var result = node.hashCode()
        result = 31 * result + trace.hashCode()
        return result
    }
}