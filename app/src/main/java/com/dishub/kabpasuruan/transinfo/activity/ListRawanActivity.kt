package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.adapter.AlternateAdapter
import com.dishub.kabpasuruan.transinfo.adapter.DaerahRawanAdapter
import com.dishub.kabpasuruan.transinfo.adapter.RawanBanjirAdapter
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.daerahRawan.ListRawan
import com.dishub.kabpasuruan.transinfo.model.daerahRawan.Rawan
import com.dishub.kabpasuruan.transinfo.model.jalanAlternate.Alternate
import com.dishub.kabpasuruan.transinfo.model.jalanAlternate.ListAlternate
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.android.synthetic.main.activity_list_rawan.*

class ListRawanActivity : AppCompatActivity() {

    companion object {
        const val laka = "LAKA"
        const val macet = "MACET"
        const val banjir = "BANJIR"
        const val alternatif = "ALTERNATE"
        const val TYPERAWAN = "typeRawan"
    }

    private lateinit var listRawan: List<Rawan>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_rawan)
        val typeRawan = intent.extras?.getString(TYPERAWAN).toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        when (typeRawan) {
            macet -> {
                supportActionBar?.title = "Rawan Macet"
                getMacetList()
            }
            laka -> {
                supportActionBar?.title = "Rawan Kecelakaan"
                getLakaList()
            }
            banjir -> {
                supportActionBar?.title = "Rawan Banjir"
                getRawanBanjir()
            }
            alternatif -> {
                supportActionBar?.title = "Jalan Alternatif"
                getAlternatif()
            }
        }
    }

    private fun getAlternatif() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val rawanList = retrofit.getJlAlternate()
        rawanList.enqueue(object : Callback<ListAlternate> {
            override fun onFailure(call: Call<ListAlternate>, t: Throwable) {
                Toast.makeText(
                    this@ListRawanActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ListAlternate>, response: Response<ListAlternate>) {
                if (response.code() == 200) {
                    val resultList = response.body()?.result as List<Alternate>
                    rv_rawan.layoutManager = LinearLayoutManager(this@ListRawanActivity)
                    rv_rawan.adapter = AlternateAdapter(resultList) {
                        val intent = Intent(this@ListRawanActivity, AlternateMapsActivity::class.java);
                        intent.putExtra(AlternateMapsActivity.ALTERNATE_EXTRA, it)
                        startActivity(intent)
                    }
                }
            }

        })
    }

    private fun getRawanBanjir() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val rawanList = retrofit.getRawanBanjir()
        rawanList.enqueue(object : Callback<ListRawan> {
            override fun onFailure(call: Call<ListRawan>, t: Throwable) {
                Toast.makeText(
                    this@ListRawanActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ListRawan>, response: Response<ListRawan>) {
                if (response.code() == 200) {
                    val resultList = response.body()?.result
                    rv_rawan.layoutManager = LinearLayoutManager(this@ListRawanActivity)
                    rv_rawan.adapter = RawanBanjirAdapter(resultList!!) {
                        val bundle = Bundle()
                        bundle.putString(CctvMapsActivity.LATPOS, it.latitude.toString())
                        bundle.putString(CctvMapsActivity.LONGPOS, it.longitude.toString())
                        bundle.putString(CctvMapsActivity.NameCCTV, it.nama)
                        startActivity(
                            Intent(this@ListRawanActivity, CctvMapsActivity::class.java).putExtras(
                                bundle
                            )
                        )
                    }
                }
            }
        })
    }

    private fun buildList() {
        rv_rawan.layoutManager = LinearLayoutManager(this)
        rv_rawan.adapter = DaerahRawanAdapter(listRawan) {
            val bundle = Bundle()
            bundle.putString(CctvMapsActivity.LATPOS, it.latitude.toString())
            bundle.putString(CctvMapsActivity.LONGPOS, it.longitude.toString())
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

    private fun getLakaList() {
        val retrofit = ApiClient().getApiClient(SupportUtil.dishubLink)
        val lakaList: Call<ListRawan> = retrofit.getRawanLaka()
        lakaList.enqueue(object : Callback<ListRawan> {

            override fun onFailure(call: Call<ListRawan>, t: Throwable) {
                Toast.makeText(
                    this@ListRawanActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<ListRawan>,
                response: Response<ListRawan>
            ) {
                if (response.body()?.result != null) {
                    listRawan = response.body()?.result as List<Rawan>
                    buildList()
                }
            }
        })
    }

    private fun getMacetList() {
        val retrofit = ApiClient().getApiClient(SupportUtil.dishubLink)
        val macetList: Call<ListRawan> = retrofit.getRawanMacet()
        macetList.enqueue(object : Callback<ListRawan> {
            override fun onFailure(call: Call<ListRawan>, t: Throwable) {
                Toast.makeText(
                    this@ListRawanActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<ListRawan>,
                response: Response<ListRawan>
            ) {
                if (response.body()?.result != null) {
                    listRawan = response.body()?.result as List<Rawan>
                    buildList()
                }
            }
        })
    }
}
