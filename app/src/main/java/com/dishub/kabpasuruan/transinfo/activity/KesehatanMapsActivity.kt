package com.dishub.kabpasuruan.transinfo.activity

import android.Manifest
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.kesehatan.ListKesehatan
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class KesehatanMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kesehatan_maps)
        supportActionBar?.title = "Pusat Kesehatan Terdekat"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.map_style))
        markLocationPuskesmas()
        // Request permission
        requestLocationPermission()
        // Add a marker in Sydney and move the camera

    }


    private fun requestLocationPermission() {
        val perms = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (EasyPermissions.hasPermissions(this, *perms)) {
            // Already have permission, do the thing
            // ...
            getCurrentLocation()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Izin untuk mengakses lokasi anda", 1, *perms)
        }
    }

    private fun markLocationPuskesmas() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val callCenterList: Call<ListKesehatan> = retrofit.getKesehatan()
        callCenterList.enqueue(object : Callback<ListKesehatan> {
            override fun onFailure(call: Call<ListKesehatan>, t: Throwable) {
                Toast.makeText(this@KesehatanMapsActivity, "Koneksi error", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<ListKesehatan>, response: Response<ListKesehatan>) {
                if (response.code() == 200) {
                    val listKesehatan = response.body() as ListKesehatan
                    listKesehatan.result!!.forEach {
                        mMap.addMarker(
                            MarkerOptions().icon(
                                BitmapDescriptorFactory.defaultMarker(
                                    BitmapDescriptorFactory.HUE_RED
                                )
                            ).position(LatLng(it.latitude!!, it.longitude!!)).title(it.namaInstansi)
                        )
                    }
                } else {
                    Toast.makeText(
                        this@KesehatanMapsActivity,
                        "Tidak bisa mendapatkan lokasi ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        // Get user location
        getCurrentLocation()
    }

    private fun getCurrentLocation() {
        // GET MY CURRENT LOCATION
        val mFusedLocation = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocation.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                val myLocation = LatLng(location.latitude, location.longitude)
                mMap.addMarker(
                    MarkerOptions().icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_YELLOW
                        )
                    ).position(myLocation).title("Lokasi Anda")
                )
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12f))
                val cu = CameraUpdateFactory.newLatLngZoom(myLocation, 16f)
                // Animate Camera
                mMap.isTrafficEnabled = true
                mMap.animateCamera(cu)
                mMap.addCircle(
                    CircleOptions()
                        .center(myLocation)
                        .radius(500.toDouble())
                        .strokeWidth(0f)
                        .fillColor(0x550000FF)
                )
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
