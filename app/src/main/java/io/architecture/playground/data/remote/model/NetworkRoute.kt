package io.architecture.playground.data.remote.model

import com.google.gson.annotations.SerializedName


data class NetworkRoute(
    @SerializedName("n") var nodeId: String,
    @SerializedName("r") var route: ArrayList<ArrayList<Double>>? // TODO Array of array is better. Avoiding autoboxing
)