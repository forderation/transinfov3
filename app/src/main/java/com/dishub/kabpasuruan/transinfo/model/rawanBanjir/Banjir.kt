package com.dishub.kabpasuruan.transinfo.model.rawanBanjir

import com.google.gson.annotations.SerializedName

data class Banjir(
    @SerializedName("nama_jalan")
    val nama: String?,
    @SerializedName("kecamatan")
    val kec: String?,
    @SerializedName("desa")
    val desa: String?,
    @SerializedName("status_jalan")
    val status: String?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)