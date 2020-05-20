package com.dishub.kabpasuruan.transinfo.model.daerahRawan

import com.google.gson.annotations.SerializedName

data class Rawan(
    @SerializedName("Nama_Jalan")
    val nama: String?,
    @SerializedName("Kecamatan")
    val kec: String?,
    @SerializedName("Desa")
    val desa: String?,
    @SerializedName("Status_Jalan")
    val status: String?,
    @SerializedName("koord_X")
    val latitude: String,
    @SerializedName("koord_Y")
    val longitude: String
)