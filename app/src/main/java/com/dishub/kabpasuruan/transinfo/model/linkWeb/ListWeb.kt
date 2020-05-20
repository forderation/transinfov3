package com.dishub.kabpasuruan.transinfo.model.linkWeb
import com.google.gson.annotations.SerializedName

data class ListWeb(
    @SerializedName("result")
    val result: List<Web>
)