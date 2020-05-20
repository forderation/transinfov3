package com.dishub.kabpasuruan.transinfo.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CctvMapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val LATPOS = "LATPOS"
        const val LONGPOS = "LONGPOS"
        const val NameCCTV = "NameCCTV"
    }

    private lateinit var mMap: GoogleMap
    private lateinit var latpost : String
    private lateinit var longpost : String
    private lateinit var nameCctv : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cctv_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        latpost = intent.extras?.getString(LATPOS).toString()
        longpost = intent.extras?.getString(LONGPOS).toString()
        nameCctv = intent.extras?.getString(NameCCTV).toString()
        supportActionBar?.title = nameCctv
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        val zoomLvl = SupportUtil.zoomLevelCctv
        mMap = googleMap
        val cctvPoint = LatLng(latpost.toDouble(), longpost.toDouble())
        mMap.addMarker(MarkerOptions().position(cctvPoint).title(this.nameCctv))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cctvPoint, zoomLvl))
        mMap.isTrafficEnabled = true
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
