package com.dishub.kabpasuruan.transinfo.utils

import android.util.Log
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.UserDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("SpellCheckingInspection")
class SupportUtil{
    companion object{
        const val siAngsaPackage = "dishub.siangsa.kabupatenpasuruan"
        const val ujiKirPackage = "com.dishub.kabpasuruan.ekir"
        const val fbLink = "https://www.facebook.com/dishubkabpas/"
        const val twitLink  = "https://twitter.com/dishubkabpas"
        const val igLink = "https://www.instagram.com/dishubkabpas/"
        const val uTubeLink = "https://www.youtube.com/channel/UCFug1KLmucORJFd7Ea8l7qw"
        const val siangsaLink = "https://siangsa.pasuruankab.go.id/"
        const val dishubLink = "https://dishub.pasuruankab.go.id/"
        const val siangsaPhoto = "https://siangsa.pasuruankab.go.id/upload/"
        const val otherDomain = "http://36.89.61.18/"
        const val kabpasLink = "https://pasuruankab.go.id/"
        const val zoomLevelCctv = 16.0f
        //  SHARED PREFERENCES
        const val spIsLoggedIn = "isLoggedIn"
        const val spIsVerified = "isVerified"
        const val spUserID = "spUserID"
        // END
        const val spToken = "spToken"
    }

}