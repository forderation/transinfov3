package com.dishub.kabpasuruan.transinfo.model.rawanBanjir

import com.google.gson.annotations.SerializedName

data class ListBanjir(
    @SerializedName("result")
    val result: List<Banjir>
)