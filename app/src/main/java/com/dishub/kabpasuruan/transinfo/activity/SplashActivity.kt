package com.dishub.kabpasuruan.transinfo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dishub.kabpasuruan.transinfo.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    companion object{
        const val USER_MESSAGE = "USER_MESSAGE"
    }
    private val timeOut:Long=2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if(intent.extras != null){
            val username = intent?.extras!!.getString(USER_MESSAGE)
            if(username!=null){
                Snackbar.make(splash_root,"Welcome: $username",Snackbar.LENGTH_LONG).show()
            }
        }
        Handler().postDelayed({
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }, timeOut)
    }
}
