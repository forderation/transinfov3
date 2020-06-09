package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.adapter.CCTVListAdapter
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.api.ApiService
import com.dishub.kabpasuruan.transinfo.model.cctv.CCTVLive
import com.dishub.kabpasuruan.transinfo.model.cctv.ListCCTV
import com.google.android.material.snackbar.Snackbar
import com.pedro.vlc.VlcListener
import com.pedro.vlc.VlcVideoLibrary
import kotlinx.android.synthetic.main.activity_live_cctv.*
import kotlinx.coroutines.*
import org.videolan.libvlc.MediaPlayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class LiveCctvActivity : AppCompatActivity() {
    private lateinit var listCCTV: ArrayList<CCTVLive>
    private lateinit var service: ApiService
    private lateinit var apiKey: String
    private lateinit var vlcListener: VlcListener
    private lateinit var apiSecret: String
    private lateinit var vlcLib: VlcVideoLibrary
    private lateinit var snackbar: Snackbar
    private var currentCctv: CCTVLive? = null
    private var isSuccessPlay = false
    private var orientation: Int = -1

    private val timer = object : CountDownTimer(6000, 1000) {
        override fun onFinish() {
            if (!isSuccessPlay) {
                snackbar.dismiss()
                Toast.makeText(applicationContext, "Pemutaran CCTV gagal", Toast.LENGTH_SHORT)
                    .show()
                player_view.visibility = View.GONE
                gesture_layout?.visibility = View.VISIBLE
            } else {
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Snackbar.make(
                        ns_scroll,
                        "Putar layar ke orientasi potrait untuk memilih CCTV",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        override fun onTick(p0: Long) {

        }
    }

    companion object {
        const val EXTRA_CCTV = "EXTRA_CCTV"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_cctv)
        orientation = resources.configuration.orientation
        player_view.visibility = View.VISIBLE
        snackbar = Snackbar.make(ns_scroll, "", Snackbar.LENGTH_INDEFINITE)
        supportActionBar?.title = getString(R.string.cctv_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        service = ApiClient().getApiClient(BuildConfig.BASE_API)
        this.apiKey = BuildConfig.API_KEY
        this.apiSecret = BuildConfig.API_SECRET
        vlcListener = object : VlcListener {
            override fun onComplete() {
                snackbar.dismiss()
                isSuccessPlay = true
                Toast.makeText(applicationContext, "Pemutaran berhasil", Toast.LENGTH_SHORT).show()
            }

            override fun onBuffering(event: MediaPlayer.Event?) {

            }

            override fun onError() {

            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            val createOptions = async {
                vlcLib = VlcVideoLibrary(this@LiveCctvActivity, vlcListener, player_view)
            }
            currentCctv = intent.getSerializableExtra(EXTRA_CCTV) as? CCTVLive
            if (currentCctv != null) {
                snackbar.setText("Sedang memutar : ${currentCctv!!.name}").show()
                gesture_layout?.visibility = View.GONE
                createOptions.await()
                timer.cancel()
                vlcLib.stop()
                vlcLib.play(currentCctv!!.url)
                timer.start()
                Log.d("cctv_debug", "playing cctv success")
            } else {
                gesture_layout?.visibility = View.VISIBLE
                player_view.visibility = View.GONE
            }
        }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView?.layoutManager = LinearLayoutManager(this)
            getListCCTV()
        } else {
            supportActionBar?.hide()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (currentCctv != null) {
            intent.putExtra(EXTRA_CCTV, currentCctv)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    private fun getListCCTV() {
        val cctvList: Call<ListCCTV> = service.cctvList(apiKey, apiSecret)
        cctvList.enqueue(object : Callback<ListCCTV> {
            override fun onFailure(call: Call<ListCCTV>, t: Throwable) {
                Toast.makeText(
                    this@LiveCctvActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ListCCTV>, response: Response<ListCCTV>) {
                val statsCode = response.body()?.status_code
                val statsMsg = response.body()?.status_message
                if (statsCode != "200") {
                    Toast.makeText(
                        this@LiveCctvActivity,
                        "Network Error : $statsCode , $statsMsg",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {
                    listCCTV = response.body()?.items as ArrayList<CCTVLive>
                    buildCctvList()
                }
            }
        })
    }

    private fun buildCctvList() {
        val adapter = CCTVListAdapter(listCCTV, {
            //change CCTV player
            GlobalScope.launch(Dispatchers.Main) {
                runOnUiThread {
                    currentCctv = it
                    val intent = Intent(this@LiveCctvActivity, LiveCctvActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra(EXTRA_CCTV, currentCctv)
                    startActivity(intent)
                }
            }
        },
            //goto maps activity to point location CCTV
            {
                val bundle = Bundle()
                bundle.putString(CctvMapsActivity.LATPOS, it.latitude)
                bundle.putString(CctvMapsActivity.LONGPOS, it.longitude)
                bundle.putString(CctvMapsActivity.NameCCTV, it.name)
                startActivity(
                    Intent(this, CctvMapsActivity::class.java).putExtras(bundle)
                )
            }
        )
        recyclerView?.adapter = adapter
    }
}