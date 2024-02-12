package io.architecture.playground.model

import io.architecture.playground.data.remote.model.ConnectionEvent

data class Connection(
    var state: ConnectionState = ConnectionState.UNDEFINED,
    val rtt: UpstreamRtt,
    val isConnected: Boolean = false
) {
    fun changeStatus(connectionEvent: ConnectionEvent) {
        // Simplification ;)
        state = connectionEvent
    }

    companion object {
        const val ROUTE_SERVICE_CONNECTION = 0
        const val TRACE_SERVICE_CONNECTION = 1
    }
}

// Simplification ;)
typealias ConnectionState = ConnectionEvent