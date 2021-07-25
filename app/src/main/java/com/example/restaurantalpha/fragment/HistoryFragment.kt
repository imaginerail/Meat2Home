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
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.activity.MainActivity
import com.example.restaurantalpha.adapter.HistoryAdapter
import com.example.restaurantalpha.model.Order
import com.example.restaurantalpha.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
    lateinit var recycleHistory: RecyclerView
    lateinit var btnExploreNow: Button
    lateinit var historyAdapter: HistoryAdapter
    lateinit var noLayout: RelativeLayout
    lateinit var imgEmptyHistory: ImageView
    lateinit var txtempty: TextView
    var ordList = arrayListOf<Order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recycleHistory = view.findViewById(R.id.recycleHistory)
        noLayout = view.findViewById(R.id.noLayout)
        imgEmptyHistory = view.findViewById(R.id.imgEmptyHistory)
        txtempty = view.findViewById(R.id.txtempty)
        btnExploreNow = view.findViewById(R.id.btnExploreNow)

        btnExploreNow.setOnClickListener {

            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()


        }

        setUpHistory()
        return view
    }

    private fun setUpHistory() {
        if (ConnectionManager().checkConnection(activity as Context)) {
            val uid = FirebaseAuth.getInstance().uid
            val ref = FirebaseDatabase.getInstance().getReference("Orders/$uid")

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        val order = it.getValue(Order::class.java)
                        ordList.add(order!!)
                    }
                    if (ordList.isEmpty()) noLayout.visibility = View.VISIBLE
                    else noLayout.visibility = View.GONE

                    if (activity != null) {
                        historyAdapter =
                            HistoryAdapter(
                                activity as Context,
                                ordList
                            )
                        val mLayoutManager = LinearLayoutManager(activity as Context)
                        recycleHistory.layoutManager = mLayoutManager
                        recycleHistory.itemAnimator = DefaultItemAnimator()
                        recycleHistory.adapter = historyAdapter
                        recycleHistory.setHasFixedSize(true)
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


//[
//    Order(
//        items=
//        [
//            {
//                menName=Rabbit Meat,
//                menPrice=599,
//                menQuantity=1,
//                menImage=https:firebasestorage.googleapis.com/v0/b/restaurantalpha-1598711137144.appspot.com/o/Menu%2FRabbit%2Frabbitmeat.jpeg?alt=media&token=139c2e31-c3f1-4cb3-9a4d-c179a62ab29d,
//                menCategory=Rabbit,
//                menId=r1
//            }
// ], timeStamp=1600845851,
//        totalCost=726
//    ), Order(
//        items=
//        [
//            {
//                menName=Biryani Cut,
//                menPrice=175,
//                menQuantity=1,
//                menImage=https:firebasestorage.googleapis.com/v0/b/restaurantalpha-1598711137144.appspot.com/o/Menu%2Fmutton%2Fbiryani%20cut.jpg?alt=media&token=a7484c01-acce-49f5-b4ef-c9a77524d3ad,
//                menCategory=Mutton,
//                menId=m1
//            },
//            {
//                menName=Boneless,
//                menPrice=399,
//                menQuantity=1,
//                menImage=https:firebasestorage.googleapis.com/v0/b/restaurantalpha-1598711137144.appspot.com/o/Menu%2Fmutton%2Fboneless.jpg?alt=media&token=6ef94c2c-5c55-4967-8918-5703a2b1093a,
//                menCategory=Mutton,
//                menId=m2
//            }
//        ], timeStamp=1600846082,
//        totalCost=727
//    )
//]
