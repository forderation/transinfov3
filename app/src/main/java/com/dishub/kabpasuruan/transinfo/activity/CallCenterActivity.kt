package com.dishub.kabpasuruan.transinfo.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.adapter.CallCenterAdapter
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.fragment.requestBody
import com.dishub.kabpasuruan.transinfo.model.OptionsResponse
import com.dishub.kabpasuruan.transinfo.model.PostResponse
import com.dishub.kabpasuruan.transinfo.model.callCenter.CallCenter
import com.dishub.kabpasuruan.transinfo.model.callCenter.ListCallCenter
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_call_center.*
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CallCenterActivity : AppCompatActivity() {
    private lateinit var listCallCenter: ListCallCenter
    private var isAuthPhone = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_center)
        supportActionBar?.title = getString(R.string.telp_penting_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        rv_cc.layoutManager = LinearLayoutManager(this)
        getOptions()
        getCallCenter()
    }

    private fun getCallCenter() {
        val retrofit = ApiClient().getApiClient(SupportUtil.otherDomain)
        val callCenterList: Call<ListCallCenter> = retrofit.getCallCenter()
        callCenterList.enqueue(object : Callback<ListCallCenter> {

            override fun onFailure(call: Call<ListCallCenter>, t: Throwable) {
                Toast.makeText(
                    this@CallCenterActivity,
                    "Error can't get response ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(
                call: Call<ListCallCenter>,
                response: Response<ListCallCenter>
            ) {
                Log.d("api_hit", response.body().toString())
                if (response.body()?.result != null) {
                    listCallCenter = response.body() as ListCallCenter
                    buildCallCenterList()
                }
            }
        })
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

    private fun buildCallCenterList() {
        val preferences = this.getSharedPreferences(
            getString(R.string.db_preferences),
            Context.MODE_PRIVATE
        )
        val isVerified = preferences.getBoolean(SupportUtil.spIsVerified, false)
        val isLogin = preferences.getBoolean(SupportUtil.spIsLoggedIn, false)
        rv_cc.adapter = CallCenterAdapter(listCallCenter.result!!) {
            if(isAuthPhone==1){
                if (isVerified && isLogin) {
                    requestLocationPermission(it)
                } else {
                    Snackbar.make(
                        root_cc,
                        "Anda belum memasukkan akun atau akun belum terverifikasi",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }else{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${it.nomorKontak}")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    private fun getCurrentLocation(callCenter: CallCenter) {
        // GET MY CURRENT LOCATION
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.lastLocation.addOnSuccessListener(this) { location ->
            if(location!=null){
                val preferences = this.getSharedPreferences(
                    getString(R.string.db_preferences),
                    Context.MODE_PRIVATE
                )
                val token = preferences.getString(SupportUtil.spToken,"")
                val userID = preferences.getInt(SupportUtil.spUserID,-1).toString()
                if(token != "" && userID != "-1"){
                    val api = ApiClient().getApiClient(BuildConfig.BASE_API)
                    val postKes = api.postCallCenter(
                        "Bearer $token",
                        location.latitude.toString().requestBody(),
                        location.longitude.toString().requestBody(),
                        userID.requestBody(),
                        callCenter.id.toString().requestBody()
                    )
                    postKes.enqueue(object :Callback<PostResponse>{
                        override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                            Snackbar.make(
                                root_cc,
                                "Error koneksi internet",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<PostResponse>,
                            response: Response<PostResponse>
                        ) {
                            if(response.code() == 201){
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.data = Uri.parse("tel:${callCenter.nomorKontak}")
                                startActivity(intent)
                            }else{
                                Snackbar.make(
                                    root_cc,
                                    "Kesalahan upload lokasi. kode error: ${response.code()}",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    })
                }else{
                    Snackbar.make(
                        root_cc,
                        "Kesalahan mendapatkan data pengguna",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }else{
                Snackbar.make(
                    root_cc,
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

    private fun requestLocationPermission(callCenter: CallCenter) {
        val perms = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (EasyPermissions.hasPermissions(this, *perms)) {
            getCurrentLocation(callCenter)
        } else {
            EasyPermissions.requestPermissions(this, "Izin untuk mengakses lokasi anda", 1, *perms)
        }
    }
}
