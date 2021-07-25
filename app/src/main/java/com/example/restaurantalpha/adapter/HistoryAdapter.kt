package com.example.restaurantalpha.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.model.FAQitems
import com.example.restaurantalpha.model.OIEntity
import com.example.restaurantalpha.model.Order
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryAdapter(val context: Context, val ordList: ArrayList<Order>) :
    RecyclerView.Adapter<HistoryAdapter.FAQViewHolder>() {
    class FAQViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtorderId: TextView = view.findViewById(R.id.txtorderId)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val recycleInside:RecyclerView=view.findViewById(R.id.recycleInside)
        val txtTotal: TextView = view.findViewById(R.id.txtTotal)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_recycler_single_row, parent, false)
        return FAQViewHolder(view)

    }

    override fun getItemCount(): Int {
        return ordList.size

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val oList = ordList[position]
        holder.txtTime.text=oList.timeStamp
        holder.txtorderId.text="ID: ${oList.orderId}"
        holder.txtTotal.text = " Total (inclusive of taxes) ₹${oList.totalCost}"
        val oeList = arrayListOf<OIEntity>()
            for(j in 0 until oList.items.size){
                val hm = oList.items[j]
                val oie = OIEntity(
                    hm["menCategory"].toString(),
                    hm["menId"].toString(),
                    hm["menImage"].toString(),
                    hm["menName"].toString(),
                    hm["menPrice"].toString(),
                    hm["menQuantity"].toString()
                )
                oeList.add(oie)
            }
        val insideAdapter =
            InsideAdapter(context, oeList)
        holder.recycleInside.adapter = insideAdapter
        val mLayoutManager = LinearLayoutManager(context)
        holder.recycleInside.layoutManager = mLayoutManager

    }
}