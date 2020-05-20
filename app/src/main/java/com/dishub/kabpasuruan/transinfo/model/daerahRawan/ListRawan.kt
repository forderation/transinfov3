package com.dishub.kabpasuruan.transinfo.model.daerahRawan
import com.google.gson.annotations.SerializedName

data class ListRawan(
    @SerializedName("result")
    val result: List<Rawan>
)