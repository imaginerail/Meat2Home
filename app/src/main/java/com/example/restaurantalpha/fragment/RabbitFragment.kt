package com.example.restaurantalpha.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.adapter.FoodAdapter
import com.example.restaurantalpha.model.Food
import com.example.restaurantalpha.util.ConnectionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class RabbitFragment : Fragment() {
    lateinit var recycleCategory: RecyclerView
    private lateinit var homeAdapter: FoodAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    val itemList = arrayListOf<Food>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_rabbit, container, false)

        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
        recycleCategory = view.findViewById(R.id.recycleCategory)
        itemList.clear()
        setUpRecycler()

        return view
    }
    private fun setUpRecycler() {
        if (ConnectionManager().checkConnection(activity as Context)) {
            val ref = FirebaseDatabase.getInstance()
                .getReference("/Menu/Rabbit")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {

                    progressLayout.visibility = View.GONE

                    p0.children.forEach {
                        val food = it.getValue(Food::class.java)
                        val fd=Food(food!!.id,"Rabbit",food.cost,food.image,food.name)
                        itemList.add(fd)
                    }



                    if (activity != null) {
                        homeAdapter =
                            FoodAdapter(
                                activity as Context,
                                itemList
                            )
                        val mLayoutManager = LinearLayoutManager(activity as Context)
                        recycleCategory.layoutManager = mLayoutManager
                        recycleCategory.itemAnimator = DefaultItemAnimator()
                        recycleCategory.adapter = homeAdapter
                        recycleCategory.setHasFixedSize(true)
                    }


                }

            })
        } else {
            checkIfNoInternet()
        }
    }

    private fun checkIfNoInternet() {
        val dialog = AlertDialog.Builder(activity as Context)
        dialog.setTitle("Failure")
        dialog.setMessage("Internet Connection NOT Found")
        dialog.setPositiveButton("Open Settings") { _, _ ->
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
            activity?.finish()
        }
        dialog.setNegativeButton("Exit") { _, _ ->
            ActivityCompat.finishAffinity(activity as Activity)
        }
        dialog.create()
        dialog.show()
    }

}