package com.dishub.kabpasuruan.transinfo.api
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class ApiClient{
    fun getApiClient(uriLink:String) : ApiService{
        val retrofit = Retrofit.Builder()
            .baseUrl(uriLink)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}