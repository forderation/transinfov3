package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.OptionsResponse
import kotlinx.android.synthetic.main.activity_pengaduan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaduanActivity : AppCompatActivity(), View.OnClickListener {

    private var optionResp: OptionsResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaduan)
        supportActionBar?.title = getString(R.string.laporan_pengaduan_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        wa_cv.isEnabled = false
        email_cv.isEnabled = false
        getOptions()
        wa_cv.setOnClickListener(this)
        email_cv.setOnClickListener(this)
    }

    private fun getOptions() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val callCenterList: Call<OptionsResponse> = retrofit.apiOption()
        callCenterList.enqueue(object : Callback<OptionsResponse> {
            override fun onFailure(call: Call<OptionsResponse>, t: Throwable) {
                Toast.makeText(
                    this@PengaduanActivity,
                    "Tidak bisa mendapat informasi pengaduan",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<OptionsResponse>,
                response: Response<OptionsResponse>
            ) {
                if (response.code() == 200) {
                    optionResp = response.body() as OptionsResponse
                    if (optionResp != null) {
                        wa_cv.isEnabled = true
                        email_cv.isEnabled = true
                        tv_email.text = optionResp!!.email
                        tv_wa.text = optionResp!!.phone
                    }
                }
            }

        })
    }

    override fun onClick(p0: View?) {
        when (p0) {
            wa_cv -> {
                val uri = Uri.parse("https://api.whatsapp.com/send?phone=" + tv_wa.text.toString())
                val sendIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(sendIntent)
            }
            email_cv -> {
                val emailIntent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", tv_email.text.toString(), null
                    )
                )
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "LAPORAN PENGADUAN")
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}
