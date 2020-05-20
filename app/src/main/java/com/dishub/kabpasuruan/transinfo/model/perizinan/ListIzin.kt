package com.dishub.kabpasuruan.transinfo.model.perizinan

import com.google.gson.annotations.SerializedName

data class ListIzin(
    @SerializedName("result")
    val result: List<Izin>?
)