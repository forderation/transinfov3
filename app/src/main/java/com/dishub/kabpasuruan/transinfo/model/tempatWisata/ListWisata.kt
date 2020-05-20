package com.dishub.kabpasuruan.transinfo.model.tempatWisata

import com.google.gson.annotations.SerializedName

data class ListWisata(
    @SerializedName("result")
    val result: List<Wisata>
)