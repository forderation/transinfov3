package com.dishub.kabpasuruan.transinfo.model.parking

import com.google.gson.annotations.SerializedName

class Parking (
    @SerializedName("Nama_Jalan")
    val namaJalan: String?,
    @SerializedName("Lokasi_Parkir")
    val lokasiParkir: String?,
    @SerializedName("Kecamatan")
    val kecataman: String?,
    @SerializedName("Desa")
    val desa: String?,
    @SerializedName("Nama_Jukir")
    val namaJukir: String?,
    @SerializedName("Nama_Pengawas")
    val namaPengawas: String?,
    @SerializedName("Foto")
    val foto: String?,
    @SerializedName("koord_X")
    val latitude: String?,
    @SerializedName("koord_Y")
    val longitude: String?,
    @SerializedName("Kapasitas")
    val kapasitas: Int?

)