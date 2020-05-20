package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dishub.kabpasuruan.transinfo.R
import kotlinx.android.synthetic.main.activity_daerah_rawan.*

class DaerahRawanActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daerah_rawan)
        supportActionBar?.title = getString(R.string.type_daerah_rawan_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        laka_cv.setOnClickListener(this)
        banjir_cv.setOnClickListener(this)
        macet_cv.setOnClickListener(this)
        alternate_cv.setOnClickListener(this)
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

    override fun onClick(p0: View?) {
        val bundle = Bundle()
        when(p0){
            laka_cv -> {
//                bundle.putString(ListRawanActivity.TYPERAWAN, ListRawanActivity.laka)
                bundle.putString(DaerahMapsActivity.TYPE_DAERAH, DaerahMapsActivity.LAKA)
                startActivity(
                    Intent(this, DaerahMapsActivity::class.java).putExtras(bundle)
                )
            }
            macet_cv -> {
//                bundle.putString(ListRawanActivity.TYPERAWAN, ListRawanActivity.macet)
                bundle.putString(DaerahMapsActivity.TYPE_DAERAH, DaerahMapsActivity.MACET)
                startActivity(
                    Intent(this, DaerahMapsActivity::class.java).putExtras(bundle)
                )
            }
            banjir_cv -> {
//                bundle.putString(ListRawanActivity.TYPERAWAN, ListRawanActivity.banjir)
                bundle.putString(DaerahMapsActivity.TYPE_DAERAH, DaerahMapsActivity.BANJIR)
                startActivity(
                    Intent(this, DaerahMapsActivity::class.java).putExtras(bundle)
                )
            }
            alternate_cv -> {
                bundle.putString(ListRawanActivity.TYPERAWAN, ListRawanActivity.alternatif)
                startActivity(
                    Intent(this, ListRawanActivity::class.java).putExtras(bundle)
                )
            }
        }
    }


}
