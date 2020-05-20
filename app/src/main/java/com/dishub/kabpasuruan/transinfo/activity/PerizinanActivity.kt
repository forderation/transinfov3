package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.OptionsResponse
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_perizinan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerizinanActivity : AppCompatActivity(), View.OnClickListener {

    private var optionResp: OptionsResponse? = null
    private val mimeType = "text/html"
    private val encoding = "UTF-8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perizinan)
        supportActionBar?.title = getString(R.string.izin_lantas)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        getOptions()
        btn_trayek.setOnClickListener(this)
        btn_andalalin.setOnClickListener(this)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    override fun onClick(p0: View?) {
        when (p0) {
            btn_trayek -> {
                val dialog = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.layanan_pop_up, null)
                dialog.setView(dialogView)
                dialog.setCancelable(true)
                dialog.setIcon(R.mipmap.ic_launcher)
                dialog.setTitle("Detail Trayek")
                val webView = dialogView.findViewById<WebView>(R.id.web_view)
                webView.loadDataWithBaseURL("", optionResp?.trayekView, mimeType, encoding, "")
                dialog.setNegativeButton("Tutup") { d, _ ->
                    d.dismiss()
                }
                dialog.setPositiveButton("Detail") { d, _ ->
                    Snackbar.make(root_izin, "Trayek belum tersedia", Snackbar.LENGTH_SHORT).show()
                    d.dismiss()
                }
                dialog.show()
            }
            btn_andalalin -> {
                val dialog = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.layanan_pop_up, null)
                dialog.setView(dialogView)
                dialog.setCancelable(true)
                dialog.setIcon(R.mipmap.ic_launcher)
                dialog.setTitle("Detail Andalalin")
                val webView = dialogView.findViewById<WebView>(R.id.web_view)
                webView.loadDataWithBaseURL("", optionResp?.andalalinView, mimeType, encoding, "")
                dialog.setNegativeButton("Tutup") { d, _ ->
                    d.dismiss()
                }
                dialog.setPositiveButton("Detail") { d, _ ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://dishub.pasuruankab.go.id/andalalin-daftar/")
                    startActivity(intent)
                    d.dismiss()
                }
                dialog.show()
            }
        }
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
                    optionResp = response.body() as OptionsResponse
                }
            }

        })
    }
}
