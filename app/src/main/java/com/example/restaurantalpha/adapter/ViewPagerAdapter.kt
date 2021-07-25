package com.example.restaurantalpha.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.restaurantalpha.fragment.*
import com.google.android.material.tabs.TabLayout

class ViewPagerAdapter(activity:AppCompatActivity,val count:Int) : FragmentStateAdapter(activity) {


    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 ->{ ChickenFragment()
            }
            1-> MuttonFragment()
            2-> RabbitFragment()
            3-> FishFragment()
            else -> {
                BeefFragment()
            }
        }
    }


}