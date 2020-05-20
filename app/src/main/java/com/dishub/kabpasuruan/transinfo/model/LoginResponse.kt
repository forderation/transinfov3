package com.dishub.kabpasuruan.transinfo.model

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @SerializedName("message")
    val message: String?
)