package com.dishub.kabpasuruan.transinfo.model.cctv

import com.google.gson.annotations.SerializedName

data class ListCCTV(
    @SerializedName("status_message")
    val status_message: String?,
    @SerializedName("items")
    val items: List<CCTVLive>?,
    @SerializedName("status_code")
    val status_code: String?
)