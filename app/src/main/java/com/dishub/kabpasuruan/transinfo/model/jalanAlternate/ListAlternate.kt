package com.dishub.kabpasuruan.transinfo.model.jalanAlternate

import com.google.gson.annotations.SerializedName

data class ListAlternate(
    @SerializedName("result")
    val result: List<Alternate>
)