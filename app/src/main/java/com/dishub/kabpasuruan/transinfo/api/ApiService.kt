package com.dishub.kabpasuruan.transinfo.api

import com.dishub.kabpasuruan.transinfo.model.*
import com.dishub.kabpasuruan.transinfo.model.callCenter.ListCallCenter
import com.dishub.kabpasuruan.transinfo.model.cctv.ListCCTV
import com.dishub.kabpasuruan.transinfo.model.daerahRawan.ListRawan
import com.dishub.kabpasuruan.transinfo.model.jalanAlternate.ListAlternate
import com.dishub.kabpasuruan.transinfo.model.kesehatan.ListKesehatan
import com.dishub.kabpasuruan.transinfo.model.linkWeb.ListWeb
import com.dishub.kabpasuruan.transinfo.model.parking.ListPark
import com.dishub.kabpasuruan.transinfo.model.perizinan.ListIzin
import com.dishub.kabpasuruan.transinfo.model.rawanBanjir.ListBanjir
import com.dishub.kabpasuruan.transinfo.model.slide.ListSlide
import com.dishub.kabpasuruan.transinfo.model.tempatWisata.ListWisata
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/cctv")
    fun cctvList(
        @Field("api_key") apiKey: String, @Field("api_secret") apiSecret: String
    ): Call<ListCCTV>

    @GET("api/options")
    fun apiOption() : Call<OptionsResponse>

    @GET("api/call_center")
    fun getCallCenter(): Call<ListCallCenter>

    @GET("api/link_web")
    fun getListWeb(): Call<ListWeb>

    @GET("api/jl_alternatif")
    fun getJlAlternate(): Call<ListAlternate>

    @GET("api/rawan_banjir")
    fun getRawanBanjir(): Call<ListBanjir>

    @GET("api/puskesmas_call")
    fun getKesehatan(): Call<ListKesehatan>

    @Multipart
    @POST("/api/izin")
    fun getIzin(
        @Part("id_user") idUser: RequestBody
    ): Call<ListIzin>

    @Multipart
    @POST("/api/post_izin")
    fun postIzin(
        @Header("Authorization") authorization: String,
        @Part("id_user") idUser: RequestBody,
        @Part("desc_izin") descIzin: RequestBody
    ): Call<PostResponse>

    @Multipart
    @POST("/api/register")
    fun registerAccount(
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("password") password: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("nomor_kontak") nomorKontak: RequestBody
    ): Call<UploadResponse>


    @GET("/api/details")
    fun userDetails(
        @Header("Authorization") authorization: String
    ): Call<UserDetails>

    @Multipart
    @POST("/api/login")
    fun login(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ): Call<LoginResponse>

    @Multipart
    @POST("/api/post_callcenter")
    fun postCallCenter(
        @Header("Authorization") authorization: String,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("id_user") id_user: RequestBody,
        @Part("id_call_center") id_call_center: RequestBody
    ):Call<PostResponse>

    @Multipart
    @POST("/api/post_puskesmas")
    fun postKesehatan(
        @Header("Authorization") authorization: String,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("id_user") id_user: RequestBody,
        @Part("id_puskesmas") id_puskesmas: RequestBody
    ):Call<PostResponse>

    @GET("edishub/tempat_parkirnya.php")
    fun getParking(): Call<ListPark>


    @GET("tempat_siangsa.php")
    fun getWisata(): Call<ListWisata>

    @GET("slide.php")
    fun getSlide(): Call<ListSlide>

    @GET("edishub/rawan_macet.php")
    fun getRawanMacet(): Call<ListRawan>

    @GET("edishub/rawanlaka.php")
    fun getRawanLaka(): Call<ListRawan>
}