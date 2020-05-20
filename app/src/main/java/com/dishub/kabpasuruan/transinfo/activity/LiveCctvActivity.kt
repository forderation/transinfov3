package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LiveCctvActivity : AppCompatActivity() {
    private lateinit var listCCTV: ArrayList<CCTVLive>
    private lateinit var service: ApiService
    private lateinit var apiKey: String
    private lateinit var vlcListener: VlcListener
    private lateinit var apiSecret: String
    private lateinit var vlcLib: VlcVideoLibrary
    private var linkState:String? = null
    private lateinit var snackbar: Snackbar

    companion object{
        const val LINK_STATE = "LINK_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_cctv)
        snackbar = Snackbar.make(ns_scroll,"",Snackbar.LENGTH_INDEFINITE)
        supportActionBar?.title = getString(R.string.cctv_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        service = ApiClient().getApiClient(BuildConfig.BASE_API)
        this.apiKey = BuildConfig.API_KEY
        this.apiSecret = BuildConfig.API_SECRET
        recyclerView.layoutManager = LinearLayoutManager(this)
        vlcListener = object : VlcListener {
            override fun onComplete() {
                snackbar.dismiss()
                Toast.makeText(applicationContext, "Pemutaran berhasil", Toast.LENGTH_SHORT).show()
            }
            override fun onError() {
                GlobalScope.launch(Dispatchers.Main) {
                    if (vlcLib.isPlaying) {
                        vlcLib.stop()
                    }
                    vlcLib = VlcVideoLibrary(applicationContext, vlcListener, player_view)
                    vlcLib.setOptions(listOf(":fullscreen"))
                    runOnUiThread {
                        snackbar.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "Error, load cctv",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        GlobalScope.launch(Dispatchers.Main) {
            vlcLib = VlcVideoLibrary(this@LiveCctvActivity, vlcListener, player_view)
            vlcLib.setOptions(listOf(":fullscreen"))
            val url = savedInstanceState?.getString(LINK_STATE)
            runOnUiThread {
                if(url!=null){
                    linkState = url
                    vlcLib.play(url)
                }
            }
        }
        getListCCTV()
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
        if(linkState != null){
            outState.putString(LINK_STATE, linkState)
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
                if (vlcLib.isPlaying) {
                    vlcLib.stop()
                }
                runOnUiThread {
                    ns_scroll.smoothScrollTo(0,0)
                    snackbar.setText("Sedang memutar : ${it.name}").show()
                    vlcLib.play(it.url)
                    linkState = it.url
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
        recyclerView.adapter = adapter
    }
}