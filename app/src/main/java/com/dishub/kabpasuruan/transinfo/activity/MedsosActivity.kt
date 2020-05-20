package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.dishub.kabpasuruan.transinfo.R
import com.dishub.kabpasuruan.transinfo.utils.SupportUtil
import kotlinx.android.synthetic.main.activity_medsos.*
class MedsosActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medsos)
        supportActionBar?.title = getString(R.string.link_medsos_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dishub_cv.setOnClickListener(this)
        twitter_cv.setOnClickListener(this)
        ig_cv.setOnClickListener(this)
        youtube_cv.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val intent = Intent(Intent.ACTION_VIEW);
        when(p0){
            dishub_cv -> {
                intent.data = Uri.parse(SupportUtil.fbLink)
                startActivity(intent)
            }
            twitter_cv -> {
                intent.data = Uri.parse(SupportUtil.twitLink)
                startActivity(intent)
            }
            ig_cv -> {
                intent.data = Uri.parse(SupportUtil.igLink)
                startActivity(intent)
            }
            youtube_cv -> {
                intent.data = Uri.parse(SupportUtil.uTubeLink)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = true
}
