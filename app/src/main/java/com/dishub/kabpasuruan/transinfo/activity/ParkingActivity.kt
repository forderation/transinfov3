package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.adapter.ParkingAdapter
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.parking.ListPark
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import kotlinx.android.synthetic.main.activity_parking.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParkingActivity : AppCompatActivity() {
    private lateinit var listPark : ListPark

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parking)
        supportActionBar?.title = getString(R.string.tempat_parkir_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        rv_park.layoutManager = LinearLayoutManager(this)
        getParkingList()
    }

    private fun getParkingList(){
        val retrofit = ApiClient().getApiClient(SupportUtil.dishubLink)
        val parking: Call<ListPark> = retrofit.getParking()
        parking.enqueue(object: Callback<ListPark> {

            override fun onFailure(call: Call<ListPark>, t: Throwable) {
                Toast.makeText(
                    this@ParkingActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<ListPark>,
                response: Response<ListPark>
            ) {
                if(response.body()?.result != null){
                    listPark = response.body() as ListPark
                    buildParkList()
                }else{
                    Toast.makeText(
                        this@ParkingActivity,
                        "Daftar Tempar Parkir Kosong",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        })
    }

    private fun buildParkList() {
        rv_park.adapter = ParkingAdapter(listPark.result!!){
            val bundle = Bundle()
            bundle.putString(CctvMapsActivity.LATPOS, it.latitude)
            bundle.putString(CctvMapsActivity.LONGPOS, it.longitude)
            bundle.putString(CctvMapsActivity.NameCCTV, it.namaJalan)
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
