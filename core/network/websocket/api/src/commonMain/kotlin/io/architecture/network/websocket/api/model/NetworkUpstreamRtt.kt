package io.architecture.network.websocket.api.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkServerTime(
    var type: String = "",
    var clientSentTime: Long = 0,
    var serverTime: Long = 0,
)

@Serializable
data class NetworkClientTime(val time: Long)