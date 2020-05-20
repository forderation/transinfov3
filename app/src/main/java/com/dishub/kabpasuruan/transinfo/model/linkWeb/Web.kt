package com.dishub.kabpasuruan.transinfo.model.linkWeb
import com.google.gson.annotations.SerializedName

data class Web(
    @SerializedName("nama_web")
    val namaWeb: String?,
    @SerializedName("desc_web")
    val descWeb: String?,
    @SerializedName("link_web")
    val linkWeb: String?
)