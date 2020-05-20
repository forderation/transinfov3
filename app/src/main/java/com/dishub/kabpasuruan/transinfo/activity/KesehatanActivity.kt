package com.dishub.kabpasuruan.transinfo.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.adapter.KesehatanAdapter
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.fragment.requestBody
import com.dishub.kabpasuruan.transinfo.model.OptionsResponse
import com.dishub.kabpasuruan.transinfo.model.PostResponse
import com.dishub.kabpasuruan.transinfo.model.kesehatan.Kesehatan
import com.dishub.kabpasuruan.transinfo.model.kesehatan.ListKesehatan
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_kesehatan.*
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class KesehatanActivity : AppCompatActivity() {

    private lateinit var listKesehatan: ListKesehatan
    private var isAuthPhone = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kesehatan)
        supportActionBar?.title = getString(R.string.pusat_kesehatan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        rv_ks.layoutManager = LinearLayoutManager(this)
        getOptions()
        cardView.setOnClickListener {
            startActivity(Intent(this, KesehatanMapsActivity::class.java))
        }
        getListKesehatan()
    }

    private fun getListKesehatan() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val callCenterList: Call<ListKesehatan> = retrofit.getKesehatan()
        callCenterList.enqueue(object : Callback<ListKesehatan> {
            override fun onFailure(call: Call<ListKesehatan>, t: Throwable) {
                Toast.makeText(this@KesehatanActivity, "Koneksi error", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ListKesehatan>, response: Response<ListKesehatan>) {
                if (response.code() == 200) {
                    listKesehatan = response.body() as ListKesehatan
                    buildKesehatanList()
                } else {
                    Toast.makeText(
                        this@KesehatanActivity,
                        "Koneksi error ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun buildKesehatanList() {
        rv_ks.adapter = KesehatanAdapter(listKesehatan.result!!, {
            val preferences = this.getSharedPreferences(
                getString(R.string.db_preferences),
                Context.MODE_PRIVATE
            )
            val dayHours = Calendar.getInstance()
            dayHours.set(Calendar.HOUR_OF_DAY, Date().hours)
            val isVerified = preferences.getBoolean(SupportUtil.spIsVerified, false)
            val isLogin = preferences.getBoolean(SupportUtil.spIsLoggedIn, false)
            if(isAuthPhone==1){
                if (isVerified && isLogin) {
                    val nowHour = dayHours.get(Calendar.HOUR_OF_DAY)
                    val nowMinute = dayHours.get(Calendar.MINUTE)
                    val now = "$nowHour$nowMinute".toInt()
                    if (now >= it.jam_buka!! && now <= it.jam_tutup!!) {
                        requestLocationPermission(it)
                    } else {
                        Snackbar.make(
                            root_ks,
                            "Mohon maaf jam saat ini diluar jam kerja",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Snackbar.make(
                        root_ks,
                        "Anda belum memasukkan akun atau akun belum terverifikasi",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }else{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${it.nomorKontak}")
                startActivity(intent)
            }
        })
        {
            val bundle = Bundle()
            bundle.putString(CctvMapsActivity.LATPOS, it.latitude.toString())
            bundle.putString(CctvMapsActivity.LONGPOS, it.longitude.toString())
            bundle.putString(CctvMapsActivity.NameCCTV, it.namaInstansi)
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

    private fun getOptions() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val optionList: Call<OptionsResponse> = retrofit.apiOption()
        optionList.enqueue(object : Callback<OptionsResponse> {
            override fun onFailure(call: Call<OptionsResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<OptionsResponse>,
                response: Response<OptionsResponse>
            ) {
                if (response.code() == 200) {
                    val optionsResp = response.body() as OptionsResponse
                    isAuthPhone = optionsResp.isPhoneAuth
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    private fun getCurrentLocation(kesehatan: Kesehatan) {
        // GET MY CURRENT LOCATION
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                val preferences = this.getSharedPreferences(
                    getString(R.string.db_preferences),
                    Context.MODE_PRIVATE
                )
                val token = preferences.getString(SupportUtil.spToken, "")
                val userID = preferences.getInt(SupportUtil.spUserID, -1).toString()
                if (token != "" && userID != "-1") {
                    val api = ApiClient().getApiClient(BuildConfig.BASE_API)
                    val postKes = api.postKesehatan(
                        "Bearer $token",
                        location.latitude.toString().requestBody(),
                        location.longitude.toString().requestBody(),
                        userID.requestBody(),
                        kesehatan.id.toString().requestBody()
                    )
                    postKes.enqueue(object : Callback<PostResponse> {
                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                            Snackbar.make(
                                root_ks,
                                "Error koneksi internet",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<PostResponse>,
                            response: Response<PostResponse>
                        ) {
                            if (response.code() == 201) {
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.data = Uri.parse("tel:${kesehatan.nomorKontak}")
                                startActivity(intent)
                            } else {
                                Snackbar.make(
                                    root_ks,
                                    "Kesalahan upload lokasi. kode error: ${response.code()}",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }

                    })
                } else {
                    Snackbar.make(
                        root_ks,
                        "Kesalahan mendapatkan data pengguna",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            } else {
                Snackbar.make(
                    root_ks,
                    "Harap nyalakan fitur gps anda",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private fun requestLocationPermission(kesehatan: Kesehatan) {
        val perms = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            getCurrentLocation(kesehatan)
        } else {
            EasyPermissions.requestPermissions(this, "Izin untuk mengakses lokasi anda", 1, *perms)
        }
    }
}
