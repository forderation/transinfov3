package com.dishub.kabpasuruan.transinfo.model.callCenter
import com.google.gson.annotations.SerializedName

data class CallCenter(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("nama_instansi")
    val namaInstansi: String?,
    @SerializedName("alamat")
    val alamat: String?,
    @SerializedName("nomor_kontak")
    val nomorKontak: String?
)