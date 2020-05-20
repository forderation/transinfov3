package com.dishub.kabpasuruan.transinfo.model.parking
import com.google.gson.annotations.SerializedName

data class ListPark(
    @SerializedName("result")
    val result: List<Parking>?
)