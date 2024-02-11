package io.architecture.playground.data.remote.model


enum class ConnectionEvent {
    UNDEFINED,
    OPENED,
    CLOSED,
    CLOSING,
    FAILED,
    MESSAGE_RECEIVED,
}

