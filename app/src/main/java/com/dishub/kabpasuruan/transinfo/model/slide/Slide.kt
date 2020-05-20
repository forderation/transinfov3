package com.dishub.kabpasuruan.transinfo.model.slide

import com.google.gson.annotations.SerializedName

data class Slide(
    @SerializedName("Gambar")
    val gambar: String?,
    @SerializedName("Judul")
    val judul: String?
)