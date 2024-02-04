package io.architecture.playground.data.remote.model

data class ConnectionState(
    val type: SocketConnectionState
)

enum class SocketConnectionState {
    UNDEFINED,
    OPENED,
    CLOSED,
    CLOSING,
    FAILED,
    MESSAGE_RECEIVED,
}