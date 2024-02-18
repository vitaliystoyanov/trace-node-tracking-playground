package io.architecture.api.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// Example of payload
//    "l": 30.52323011454044,
//    "lt": 42.725705657669245,
//    "s": 13,
//    "az": 3.4595129834094243,
//    "a": 0.1,
//    "t": 1706894164046,
//    "n": "bb0c326c-c1ee-11ee-b86a-d106324846e3",
//    "m": 1

@Serializable
data class NetworkTrace(
    @SerializedName("type") var type: String = "",
    @SerializedName("l") var lon: Double = 0.0,
    @SerializedName("lt") var lat: Double = 0.0,
    @SerializedName("s") var speed: Int = 0,
    @SerializedName("az") var azimuth: Double = 0.0,
    @SerializedName("a") var alt: Double = 0.0,
    @SerializedName("t") var sentAtTime: Long = 0,
    @SerializedName("n") var nodeId: String = "",
    @SerializedName("m") var mode: Int = 0
)