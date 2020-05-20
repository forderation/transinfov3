package com.dishub.kabpasuruan.transinfo.model.jalanAlternate

import com.google.gson.annotations.SerializedName

data class Alternate(
    @SerializedName("nama_jalan")
    val nama: String?,
    @SerializedName("kecamatan")
    val kec: String?,
    @SerializedName("desa")
    val desa: String?,
    @SerializedName("status_jalan")
    val status: String?,
    @SerializedName("latitude")
    val latitude: String?,
    @SerializedName("longitude")
    val longitude: String?,
    @SerializedName("info_tambahan")
    val informasi: String?
)