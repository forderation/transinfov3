package com.dishub.kabpasuruan.transinfo.model.tempatWisata

import com.google.gson.annotations.SerializedName

data class Wisata(
    @SerializedName("Nama_Tempat")
    val nama: String?,
    @SerializedName("Alamat")
    val alamat: String?,
    @SerializedName("thumnail")
    val thumnail: String?,
    @SerializedName("koord_x")
    val latitude: String?,
    @SerializedName("koord_y")
    val longitude: String?
)