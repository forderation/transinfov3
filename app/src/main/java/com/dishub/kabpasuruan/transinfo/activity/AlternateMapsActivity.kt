package com.dishub.kabpasuruan.transinfo.activity

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.model.jalanAlternate.Alternate
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import pub.devrel.easypermissions.EasyPermissions

class AlternateMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val ALTERNATE_EXTRA = "ALTERNATE_EXTRA"
    }

    private lateinit var mMap: GoogleMap
    private var alternate: Alternate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daerah_maps)
        supportActionBar?.title = "Jalan Alternatif"
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
    }

    private fun markLocation() {
        alternate = intent.getSerializableExtra(ALTERNATE_EXTRA) as? Alternate
        if (alternate != null) {
            supportActionBar?.title = alternate!!.nama
            val roadMap = PolylineOptions().width(5f)
            alternate!!.locations.forEachIndexed { i, it ->
                val point = LatLng(it.latitude, it.longitude)
                roadMap.add(point)
                val idx = i + 1
                mMap.addMarker(
                    MarkerOptions().icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED
                        )
                    ).position(LatLng(it.latitude, it.longitude)).title("#$idx")
                )
            }
            mMap.addPolyline(roadMap)
        }
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
                val cu = CameraUpdateFactory.newLatLngZoom(myLocation, 16f)
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
