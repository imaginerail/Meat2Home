package com.example.restaurantalpha.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.restaurantalpha.R

class CurrentLocationActivity : AppCompatActivity() {

    lateinit var btnCurrentLocation:Button
    lateinit var txtAddress:TextView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("Location Preferences", Context.MODE_PRIVATE)

        setContentView(R.layout.activity_current_location)

        btnCurrentLocation=findViewById(R.id.btnCurrentLocation)
        txtAddress=findViewById(R.id.txtAddress)

        txtAddress.text="Your Save Address:-\n ${sharedPreferences.getString("Location", "Hyderabad")}"
        btnCurrentLocation.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(this,AccessLocationActivity::class.java))
            finishAffinity()
        }
    }
}