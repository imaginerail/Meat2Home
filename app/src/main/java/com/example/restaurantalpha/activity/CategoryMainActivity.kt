package com.example.restaurantalpha.activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.restaurantalpha.R
import com.example.restaurantalpha.adapter.ViewPagerAdapter
import com.example.restaurantalpha.fragment.CartFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
//import kotlinx.android.synthetic.main.activity_main.*

class CategoryMainActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var viewpager: ViewPager2
    lateinit var tablayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_main)
        toolbar = findViewById(R.id.toolbar)
        viewpager = findViewById(R.id.viewpager)
        tablayout = findViewById(R.id.tablayout)
        setUpToolbar()
        setUpAdapter()
        setUpTabLayout()

        if (intent != null) {
            getThere()
        }

    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
    }

    private fun setUpAdapter() {
        val fragAdapter = ViewPagerAdapter(this, 5)
        viewpager.adapter = fragAdapter

    }

    private fun setUpTabLayout() {
        TabLayoutMediator(tablayout, viewpager) { tab, _ ->
            viewpager.setCurrentItem(tab.position, true)
        }.attach()

        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                //
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewpager.currentItem = tab!!.position
            }

        })
        tablayout.getTabAt(0)?.setCustomView(R.layout.chicken)
        tablayout.getTabAt(1)?.setCustomView(R.layout.mutton)
        tablayout.getTabAt(2)?.setCustomView(R.layout.rabbit)
        tablayout.getTabAt(3)?.setCustomView(R.layout.fish)
        tablayout.getTabAt(4)?.setCustomView(R.layout.beef)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.food_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.search -> {
                startActivity(Intent(this, SearchActivity::class.java))
            }
            R.id.profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.cart -> {
                val i = Intent(this, MainActivity::class.java)
                i.putExtra("requestCM", 1)
                startActivity(i)

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getThere() {
        val pos: Int = when (intent.getStringExtra("tabposition")) {
            "Chicken" -> 0
            "Mutton" -> 1
            "Rabbit" -> 2
            "Sea Food" -> 3
            "Fish" -> 3
            else -> 4
        }
        viewpager.currentItem = pos


    }
}
