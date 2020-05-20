package com.dishub.kabpasuruan.transinfo.model

import com.google.gson.annotations.SerializedName

data class OptionsResponse(
    @SerializedName("link")
    val link: String,
    @SerializedName("news_ticker")
    val news_ticker: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("is_streaming")
    val is_streaming: Int,
    @SerializedName("isPhoneAuth")
    val isPhoneAuth: Int,
    @SerializedName("andalalinView")
    val andalalinView: String,
    @SerializedName("trayekView")
    val trayekView: String
)