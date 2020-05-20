package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.linkWeb.ListWeb
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import kotlinx.android.synthetic.main.activity_website.*
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.adapter.LinkWebAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WebsiteActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var listWeb:ListWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_website)
        supportActionBar?.title = getString(R.string.info_web_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        kabpas_cv.setOnClickListener(this)
        dishub_cv.setOnClickListener(this)
        getLinkWebsites()
    }

    private fun getLinkWebsites() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val listWebApi = retrofit.getListWeb()
        listWebApi.enqueue(object: Callback<ListWeb> {
            override fun onFailure(call: Call<ListWeb>, t: Throwable) {
                Toast.makeText(
                    this@WebsiteActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ListWeb>, response: Response<ListWeb>) {
                if(response.code()==200){
                    listWeb = response.body() as ListWeb
                    buildListWeb()
                }
            }
        })
    }

    private fun buildListWeb() {
        rv_web.layoutManager = LinearLayoutManager(this)
        rv_web.adapter = LinkWebAdapter(listWeb.result){
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it.linkWeb)
            startActivity(intent)
        }
    }

    override fun onClick(p0: View?) {
        val intent = Intent(Intent.ACTION_VIEW)
        when(p0) {
            dishub_cv -> {
                intent.data = Uri.parse(SupportUtil.dishubLink)
                startActivity(intent)
            }
            kabpas_cv -> {
                intent.data = Uri.parse(SupportUtil.kabpasLink)
                startActivity(intent)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = true
}
