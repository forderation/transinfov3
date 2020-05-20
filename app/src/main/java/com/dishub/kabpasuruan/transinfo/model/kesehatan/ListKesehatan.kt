package com.dishub.kabpasuruan.transinfo.model.kesehatan

import com.google.gson.annotations.SerializedName

data class ListKesehatan(
    @SerializedName("result")
    val result: List<Kesehatan>?
)