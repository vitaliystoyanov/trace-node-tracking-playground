package io.architecture.playground.data.remote.model

import com.google.gson.annotations.SerializedName


data class NetworkServerTime(
    @SerializedName("type") var type: String = "",
    @SerializedName("clientSentTime") var clientSentTime: Long = 0,
    @SerializedName("serverTime") var serverTime: Long = 0
)

data class NetworkClientTime(val time: Long)