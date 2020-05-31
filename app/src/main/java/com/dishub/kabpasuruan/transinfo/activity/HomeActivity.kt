package com.dishub.kabpasuruan.transinfo.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.OptionsResponse
import com.dishub.kabpasuruan.transinfo.model.slide.ListSlide
import com.dishub.kabpasuruan.transinfo.model.slide.Slide
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private var optionsResp: OptionsResponse? = null
    private lateinit var listCarousel: List<Slide>
    private var loggedIn = false
    private var isPhoneAuth = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.account_menu -> {
                startActivity(Intent(this, AccountActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = "Dinas Perhubungan TransInfo"
        lifecycle.addObserver(youtube_player_view)
        carouselView.visibility = View.GONE
        youtube_player_view.visibility = View.GONE

        val preferences = getSharedPreferences(
            getString(R.string.db_preferences),
            Context.MODE_PRIVATE
        )
        loggedIn = preferences!!.getBoolean(SupportUtil.spIsLoggedIn, false)
        live_cctv_cv.setOnClickListener(this)
        tempat_parkir_cv.setOnClickListener(this)
        si_angsa_cv.setOnClickListener(this)
        medsos_cv.setOnClickListener(this)
        link_web_cv.setOnClickListener(this)
        link_perizinan_cv.setOnClickListener(this)
        uji_kir_cv.setOnClickListener(this)
        telepon_penting.setOnClickListener(this)
        tempat_wisata_cv.setOnClickListener(this)
        daerah_rawan_cv.setOnClickListener(this)
        pengaduan_cv.setOnClickListener(this)
        pusat_kesehatan_cv.setOnClickListener(this)
        getSlide()
        getOptions()
    }

    private fun setCarousel() {
        carouselView.setImageListener { position, imageView ->
            Picasso.get()
                .load(listCarousel[position].gambar)
                .fit()
                .centerCrop()
                .into(imageView)
        }
        carouselView.pageCount = listCarousel.size
    }

    private fun getSlide() {
        val retrofit = ApiClient().getApiClient(SupportUtil.otherDomain)
        val callCenterList: Call<ListSlide> = retrofit.getSlide()
        callCenterList.enqueue(object : Callback<ListSlide> {
            override fun onFailure(call: Call<ListSlide>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<ListSlide>,
                response: Response<ListSlide>
            ) {
                if (response.body()?.result != null) {
                    listCarousel = response.body()?.result as List<Slide>
                    setCarousel()
                }
            }
        })
    }

    private fun getOptions() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val callCenterList: Call<OptionsResponse> = retrofit.apiOption()
        callCenterList.enqueue(object : Callback<OptionsResponse> {
            override fun onFailure(call: Call<OptionsResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<OptionsResponse>,
                response: Response<OptionsResponse>
            ) {
                if (response.code() == 200) {
                    optionsResp = response.body() as OptionsResponse
                    tv_news.text = optionsResp!!.news_ticker
                    tv_news.isSelected = true
                    isPhoneAuth = optionsResp!!.isPhoneAuth
                    if (optionsResp!!.is_streaming == 1) {
                        youtube_player_view.addYouTubePlayerListener(object :
                            AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.loadVideo(optionsResp!!.link, 0f)
                            }
                        })
                        youtube_player_view.visibility = View.VISIBLE
                    } else {
                        carouselView.visibility = View.VISIBLE
                    }
                }
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        youtube_player_view.release()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            pengaduan_cv -> {
                startActivity(Intent(this, PengaduanActivity::class.java))
            }
            live_cctv_cv -> {
                startActivity(Intent(this, LiveCctvActivity::class.java))
            }
            link_web_cv -> {
                startActivity(Intent(this, WebsiteActivity::class.java))
            }
            si_angsa_cv -> {
                val intent = packageManager.getLaunchIntentForPackage(SupportUtil.siAngsaPackage)
                if (intent == null) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + SupportUtil.siAngsaPackage)
                        )
                    )
                } else {
                    startActivity(intent)
                }
            }
            uji_kir_cv -> {
                val intent = packageManager.getLaunchIntentForPackage(SupportUtil.ujiKirPackage)
                if (intent == null) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + SupportUtil.ujiKirPackage)
                        )
                    )
                } else {
                    startActivity(intent)
                }
            }
            pusat_kesehatan_cv -> {
                if (isPhoneAuth == 1) {
                    if (loggedIn) {
                        startActivity(Intent(this, KesehatanActivity::class.java))
                    } else {
                        startActivity(Intent(this, AccountActivity::class.java))
                    }
                } else {
                    startActivity(Intent(this, KesehatanActivity::class.java))
                }
            }
            daerah_rawan_cv -> {
                startActivity(Intent(this, DaerahRawanActivity::class.java))
            }
            medsos_cv -> {
                startActivity(Intent(this, MedsosActivity::class.java))
            }
            telepon_penting -> {
                if (isPhoneAuth == 1) {
                    if (loggedIn) {
                        startActivity(Intent(this, CallCenterActivity::class.java))
                    } else {
                        startActivity(Intent(this, AccountActivity::class.java))
                    }
                } else {
                    startActivity(Intent(this, CallCenterActivity::class.java))
                }
            }
            tempat_parkir_cv -> {
                Snackbar.make(root_home,"Coming Soon", Snackbar.LENGTH_SHORT).show()
//                startActivity(Intent(this, ParkingActivity::class.java))
            }
            link_perizinan_cv -> {
                startActivity(Intent(this, PerizinanActivity::class.java))
            }
            tempat_wisata_cv -> {
                startActivity(Intent(this, TempatWisataActivity::class.java))
            }
        }
    }
}
