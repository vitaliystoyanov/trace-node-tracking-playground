package io.architecture.playground.data.remote

import com.google.gson.annotations.SerializedName

data class NetworkDiverTrace(

    @SerializedName("class") var id: String = "",
    @SerializedName("ept") var ept: Double = 0.0,
    @SerializedName("eps") var eps: Double = 0.0,
    @SerializedName("epv") var epv: Int = 0,
    @SerializedName("lon") var lon: Double = 0.0,
    @SerializedName("time") var time: String,
    @SerializedName("epd") var epd: Double = 0.0,
    @SerializedName("epx") var epx: Int = 0,
    @SerializedName("speed") var speed: Double = 0.0,
    @SerializedName("alt") var alt: Int = 0,
    @SerializedName("epy") var epy: Int = 0,
    @SerializedName("track") var track: Double = 0.0,
    @SerializedName("lat") var lat: Double = 0.0,
    @SerializedName("mode") var mode: Int = 0

)