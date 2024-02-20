package io.architecture.network.websocket.api.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkServerTime(
    @SerializedName("type") var type: String = "",
    @SerializedName("clientSentTime") var clientSentTime: Long = 0,
    @SerializedName("serverTime") var serverTime: Long = 0
)

@Serializable
data class NetworkClientTime(val time: Long)