package com.dishub.kabpasuruan.transinfo.activity

import android.Manifest
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dishub.kabpasuruan.transinfo.BuildConfig
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.api.ApiClient
import com.dishub.kabpasuruan.transinfo.model.daerahRawan.ListRawan
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
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

class DaerahMapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    companion object {
        const val TYPE_DAERAH = "TYPE_DAERAH"
        const val LAKA = "LAKAMAPS"
        const val BANJIR = "BANJIRMAPS"
        const val MACET = "MACETMAPS"
    }

    private lateinit var mMap: GoogleMap

    private lateinit var typeDaerah: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daerah_maps)
        typeDaerah = intent.extras?.getString(TYPE_DAERAH).toString()
        when (typeDaerah) {
            LAKA -> {
                supportActionBar?.title = "Daerah Rawan Kecelakaan"
            }
            BANJIR -> {
                supportActionBar?.title = "Daerah Rawan Banjir"
            }
            MACET -> {
                supportActionBar?.title = "Daerah Rawan Macet"
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.map_style))
        requestLocationPermission()
        markLocation()
        mMap.setOnMarkerClickListener(this)
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

    private fun markLocation() {
        when (typeDaerah) {
            BANJIR -> {
                getRawanBanjir()
            }
            LAKA -> {
                getRawanLaka()
            }
            MACET -> {
                getRawanMacet()
            }
        }
    }

    private fun getRawanBanjir() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val listRawan = retrofit.getRawanBanjir()
        listRawan.enqueue(object : Callback<ListRawan> {
            override fun onFailure(call: Call<ListRawan>, t: Throwable) {
                Toast.makeText(this@DaerahMapsActivity, "Koneksi error", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<ListRawan>, response: Response<ListRawan>) {
                val listOfRawan = response.body() as ListRawan
                listOfRawan.result.forEach {
                    val title = "${it.nama}:${it.desa}:${it.kecamatan}:${it.status}"
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                            )
                        ).position(LatLng(it.latitude, it.longitude)).title(title)
                    )
                }
            }
        })
    }

    private fun getRawanLaka() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val listRawan = retrofit.getRawanLaka()
        listRawan.enqueue(object: Callback<ListRawan> {
            override fun onFailure(call: Call<ListRawan>, t: Throwable) {
                Toast.makeText(this@DaerahMapsActivity, "Koneksi error", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<ListRawan>, response: Response<ListRawan>) {
                val listOfRawan = response.body() as ListRawan
                listOfRawan.result.forEach {
                    val title = "${it.nama}:${it.desa}:${it.kecamatan}:${it.status}"
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                            )
                        ).position(LatLng(it.latitude, it.longitude)).title(title)
                    )
                }
            }
        })
    }

    private fun getRawanMacet() {
        val retrofit = ApiClient().getApiClient(BuildConfig.BASE_API)
        val listRawan = retrofit.getRawanMacet()
        listRawan.enqueue(object : Callback<ListRawan> {
            override fun onFailure(call: Call<ListRawan>, t: Throwable) {
                Toast.makeText(this@DaerahMapsActivity, "Koneksi error", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(call: Call<ListRawan>, response: Response<ListRawan>) {
                val listOfRawan = response.body() as ListRawan
                listOfRawan.result.forEach {
                    val title = "${it.nama}:${it.desa}:${it.kecamatan}:${it.status}"
                    mMap.addMarker(
                        MarkerOptions().icon(
                            BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED
                            )
                        ).position(LatLng(it.latitude, it.longitude)).title(title)
                    )
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
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
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

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0 != null ) {
            if(p0.title != "Lokasi Anda"){
                val dialog = AlertDialog.Builder(this)
                val dialogView = layoutInflater.inflate(R.layout.maps_pop_up, null)
                dialog.setView(dialogView)
                dialog.setCancelable(true)
                dialog.setIcon(R.mipmap.ic_launcher)
                dialog.setTitle("Detail Daerah")
                val listString = p0.title.split(":")
                val namaJalan = dialogView.findViewById<TextView>(R.id.popup_nama_jalan)
                val desa = dialogView.findViewById<TextView>(R.id.popup_desa)
                val kecamatan = dialogView.findViewById<TextView>(R.id.popup_kecamatan)
                val statusJalan = dialogView.findViewById<TextView>(R.id.popup_status_jalan)
                namaJalan.text = listString[0]
                desa.text = listString[1]
                kecamatan.text = listString[2]
                statusJalan.text = listString[3]
                dialog.setPositiveButton("Tutup") { d, _ ->
                    d.dismiss()
                }
                dialog.show()
                return true
            }
        }
        return false
    }
}
