package com.dishub.kabpasuruan.transinfo.model.slide

import com.google.gson.annotations.SerializedName

data class ListSlide(
    @SerializedName("result")
    val result: List<Slide>
)