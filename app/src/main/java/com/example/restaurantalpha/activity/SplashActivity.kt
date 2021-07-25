package com.example.restaurantalpha.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.example.restaurantalpha.R

class SplashActivity : AppCompatActivity() {

    lateinit var imgSplash:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        imgSplash=findViewById(R.id.imgSplash)

        Handler().postDelayed({
            startActivity(Intent(this, AccessLocationActivity::class.java))
            finish()
        }, 3000)
    }
}