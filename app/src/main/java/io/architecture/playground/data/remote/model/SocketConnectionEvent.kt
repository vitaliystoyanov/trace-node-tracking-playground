package io.architecture.playground.data.remote.model

import kotlinx.serialization.Serializable


@Serializable
enum class ConnectionEvent {
    UNDEFINED,
    OPENED,
    CLOSED,
    CLOSING,
    FAILED,
    MESSAGE_RECEIVED
}

