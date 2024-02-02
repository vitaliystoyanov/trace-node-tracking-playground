package io.architecture.playground.data.remote.model

import com.google.gson.annotations.SerializedName


//    "l": 30.52323011454044,
//    "lt": 42.725705657669245,
//    "s": 13,
//    "b": 3.4595129834094243,
//    "a": 0.1,
//    "t": 1706894164046,
//    "n": "bb0c326c-c1ee-11ee-b86a-d106324846e3",
//    "m": 1

data class NetworkTrace(

    @SerializedName("l")     var lon      : Double   = 0.0,
    @SerializedName("lt")    var lat      : Double   = 0.0,
    @SerializedName("s")   var speed      : Int      = 0,
    @SerializedName("b") var bearing      : Double   = 0.0,
    @SerializedName("a")     var alt      : Double   = 0.0,
    @SerializedName("t")    var time      : Long     = 0,
    @SerializedName("n")  var nodeId      : String   = "",
    @SerializedName("m")    var mode      : Int      = 0

)