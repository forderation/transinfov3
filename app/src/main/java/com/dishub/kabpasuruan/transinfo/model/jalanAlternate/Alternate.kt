package com.dishub.kabpasuruan.transinfo.model.jalanAlternate

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Alternate(
    @SerializedName("nama_jalan")
    val nama: String?,
    @SerializedName("status_jalan")
    val status: String?,
    @SerializedName("info_tambahan")
    val informasi: String?,
    @SerializedName("path_locations")
    val locations: List<PathLocation>
) : Serializable