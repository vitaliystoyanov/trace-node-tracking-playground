package io.architecture.model


data class Connection(
    var state: State = State.UNDEFINED,
    var rtt: UpstreamRtt,
    var isConnected: Boolean = false
) {
    enum class State {
        UNDEFINED,
        OPENED,
        CLOSED,
        CLOSING,
        FAILED,
        MESSAGE_RECEIVED
    }
}



