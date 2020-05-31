package com.dishub.kabpasuruan.transinfo.model.daerahRawan

import com.google.gson.annotations.SerializedName

data class Rawan(
    @SerializedName("nama_jalan")
    val nama: String?,
    @SerializedName("kecamatan")
    val kecamatan: String?,
    @SerializedName("desa")
    val desa: String?,
    @SerializedName("status_jalan")
    val status: String?,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double
)