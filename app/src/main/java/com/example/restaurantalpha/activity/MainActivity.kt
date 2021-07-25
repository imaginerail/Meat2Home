package com.example.restaurantalpha.activity


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.example.restaurantalpha.R
import com.example.restaurantalpha.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var bnv: BottomNavigationView
    lateinit var frame: FrameLayout
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout

    lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("Location Preferences", Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        bnv = findViewById(R.id.bnv)
        frame = findViewById(R.id.frame)
        setUpBNV()
        openHome()
        toolbar.setOnClickListener {
            startActivity(Intent(this, CurrentLocationActivity::class.java))
        }
        setUpToolbar()

        if (intent != null) {
            if (intent.getIntExtra("requestCM", 0) == 1) {
                openCart()

            }
        }
    }

    private fun setUpBNV() {
        bnv.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    openHome()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavouriteFragment()
                        ).commit()
                }
                R.id.cart -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            CartFragment(), "CartFrag"
                        ).commit()
                }
                R.id.orderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            HistoryFragment()
                        ).commit()

                }
                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            AboutFragment()
                        ).commit()
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

    }


    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "${sharedPreferences.getString("Location", "Hyderabad")}"


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.location -> {
                startActivity(Intent(this, CurrentLocationActivity::class.java))
            }
            R.id.profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.cart -> {
                openCart()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openCart() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                CartFragment()
            ).commit()
        bnv.menu.getItem(2).isChecked = true
    }

    private fun openHome() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                HomeFragment()
            ).commit()

    }

    override fun onBackPressed() {
        val selectedItemId = bnv.selectedItemId
        if (R.id.home != selectedItemId) {
            openHome()
            bnv.menu.getItem(0).isChecked = true

        } else
            super.onBackPressed()
    }


}
