package com.dishub.kabpasuruan.transinfo.model.cctv

import com.google.gson.annotations.SerializedName

data class CCTVLive(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("longitude")
    val longitude: String?
)