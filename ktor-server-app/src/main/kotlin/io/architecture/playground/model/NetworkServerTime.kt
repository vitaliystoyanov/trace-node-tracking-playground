package io.architecture.playground.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkServerTime(
    var type: String = "",
    var clientSentTime: Long = 0,
    var serverTime: Long = 0
)