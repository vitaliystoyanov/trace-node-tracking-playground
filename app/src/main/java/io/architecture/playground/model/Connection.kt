package io.architecture.playground.model

import io.architecture.playground.data.mapping.toExternalState
import io.architecture.playground.data.remote.model.ConnectionEvent

data class Connection(
    var state: State = State.UNDEFINED,
    var rtt: UpstreamRtt,
    var isConnected: Boolean = false
) {
    fun changeState(connectionEvent: ConnectionEvent): Connection = with(this) {
        state = connectionEvent.toExternalState()
        this
    }

    enum class State {
        UNDEFINED,
        OPENED,
        CLOSED,
        CLOSING,
        FAILED,
        MESSAGE_RECEIVED
    }
}



