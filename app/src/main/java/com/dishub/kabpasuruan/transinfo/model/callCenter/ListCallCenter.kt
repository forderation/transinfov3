package com.dishub.kabpasuruan.transinfo.model.callCenter

import com.google.gson.annotations.SerializedName

data class ListCallCenter(
    @SerializedName("result")
    val result: List<CallCenter>?
)