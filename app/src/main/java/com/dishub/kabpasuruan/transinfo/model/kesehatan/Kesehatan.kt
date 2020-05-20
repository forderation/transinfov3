package com.dishub.kabpasuruan.transinfo.model.kesehatan

import com.google.gson.annotations.SerializedName

data class Kesehatan(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nama_instansi")
    val namaInstansi: String?,
    @SerializedName("alamat")
    val alamat: String?,
    @SerializedName("nomor_kontak")
    val nomorKontak: String?,
    @SerializedName("jam_buka")
    val jam_buka: Int?,
    @SerializedName("jam_tutup")
    val jam_tutup: Int?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?
)