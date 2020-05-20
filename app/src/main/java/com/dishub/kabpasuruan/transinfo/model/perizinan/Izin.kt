package com.dishub.kabpasuruan.transinfo.model.perizinan

import com.google.gson.annotations.SerializedName

data class Izin(
    @SerializedName("desc_izin")
    val desc: String?,
    @SerializedName("id_user")
    val idUser: Int?,
    @SerializedName("created_at")
    val createdAt: String?
)