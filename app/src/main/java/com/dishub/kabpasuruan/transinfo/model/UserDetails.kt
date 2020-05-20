package com.dishub.kabpasuruan.transinfo.model

import com.google.gson.annotations.SerializedName

data class UserDetails(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("photo")
    val photo: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("nomor_kontak")
    val nomorKontak: String,
    @SerializedName("activated")
    val activated: String
)