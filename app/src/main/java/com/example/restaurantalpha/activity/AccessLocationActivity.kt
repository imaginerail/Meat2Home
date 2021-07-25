package com.example.restaurantalpha.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.restaurantalpha.BuildConfig
import com.example.restaurantalpha.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import java.util.*

class AccessLocationActivity : AppCompatActivity() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    var mLastLocation: Location? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var imgMeat2home: ImageView
    lateinit var txtaskpermission: TextView
    lateinit var btnAccessLocation: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("Location Preferences", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_access_location)
        val gotLocation = sharedPreferences.getBoolean("gotLocation", false)
        if (gotLocation) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
        imgMeat2home = findViewById(R.id.imgMeat2home)
        txtaskpermission = findViewById(R.id.txtaskpermission)
        btnAccessLocation = findViewById(R.id.btnAccessLocation)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        btnAccessLocation.setOnClickListener{
            if(checkPermissions()){
                getLastLocation()
            }
            else{
                requestPermissions()
            }
        }
    }




    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result

                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(
                        (mLastLocation)!!.latitude,
                        (mLastLocation)!!.longitude,
                        1
                    )
                    val address = addresses[0]
                    val pata = "${address.getAddressLine(0)}\n${address.locality}"
                    sharedPreferences.edit().putString("Location", pata).apply()
                    sharedPreferences.edit().putBoolean("gotLocation", true).apply()
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else {
                    Log.d("TAG", "getLastLocation:exception", task.exception)
                }
            }
    }




    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }


    companion object {

        private const val TAG = "LocationProvider"

        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}