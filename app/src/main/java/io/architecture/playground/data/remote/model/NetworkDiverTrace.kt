package io.architecture.playground.data.remote.model

import com.google.gson.annotations.SerializedName

data class NetworkDiverTrace(

    @SerializedName("class") var id: String,
    @SerializedName("ept") var ept: Double,
    @SerializedName("eps") var eps: Double,
    @SerializedName("epv") var epv: Int,
    @SerializedName("lon") var lon: Double,
    @SerializedName("time") var time: String,
    @SerializedName("epd") var epd: Double,
    @SerializedName("epx") var epx: Int,
    @SerializedName("speed") var speed: Double ,
    @SerializedName("alt") var alt: Int,
    @SerializedName("epy") var epy: Int,
    @SerializedName("track") var track: Double,
    @SerializedName("lat") var lat: Double,
    @SerializedName("mode") var mode: Int

)