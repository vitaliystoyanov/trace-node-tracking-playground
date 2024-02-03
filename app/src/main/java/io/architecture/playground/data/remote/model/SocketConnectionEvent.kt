package io.architecture.playground.data.remote.model

data class NetworkConnectionEvent(
    val type: SocketConnectionEventType
)

enum class SocketConnectionEventType {
    Undefined,
    Opened,
    Closed,
    Closing,
    Failed,
    MessageReceived,
}