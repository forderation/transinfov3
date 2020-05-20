package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.adapter.WisataAdapter
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.tempatWisata.ListWisata
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import kotlinx.android.synthetic.main.activity_tempat_wisata.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TempatWisataActivity : AppCompatActivity() {
    private lateinit var listWisata : ListWisata

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tempat_wisata)
        supportActionBar?.title = getString(R.string.tempat_wisata_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        rv_wisata.layoutManager = LinearLayoutManager(this)
        getTempatWisata()
    }

    private fun getTempatWisata(){
        val retrofit = ApiClient().getApiClient(SupportUtil.siangsaLink)
        val wisataList: Call<ListWisata> = retrofit.getWisata()
        wisataList.enqueue(object: Callback<ListWisata> {
            override fun onFailure(call: Call<ListWisata>, t: Throwable) {
                Toast.makeText(
                    this@TempatWisataActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
            override fun onResponse(
                call: Call<ListWisata>,
                response: Response<ListWisata>
            ) {
                if(response.body()?.result != null){
                    listWisata = response.body() as ListWisata
                    buildListWisata()
                }
            }

        })
    }

    private fun buildListWisata() {
        rv_wisata.adapter = WisataAdapter(listWisata.result){
            val bundle = Bundle()
            bundle.putString(CctvMapsActivity.LATPOS, it.latitude)
            bundle.putString(CctvMapsActivity.LONGPOS, it.longitude)
            bundle.putString(CctvMapsActivity.NameCCTV, it.nama)
            startActivity(
                Intent(this, CctvMapsActivity::class.java).putExtras(bundle)
            )
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
