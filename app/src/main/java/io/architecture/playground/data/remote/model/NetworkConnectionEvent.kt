package io.architecture.playground.data.remote.model

data class NetworkConnectionEvent(
    val type: NetworkConnectionEventType
)

enum class NetworkConnectionEventType {
    ConnectionOpened,
    ConnectionClosed,
    ConnectionClosing,
    ConnectionFailed,
    MessageReceived,
}