package com.example.restaurantalpha.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.model.OIEntity

class InsideAdapter(val context: Context, val iList: ArrayList<OIEntity>) :
        RecyclerView.Adapter<InsideAdapter.FAQViewHolder>() {
        class FAQViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val txtSno: TextView = view.findViewById(R.id.txtSno)
            val txtMenuName: TextView = view.findViewById(R.id.txtMenuName)
            val txtMenuCost: TextView = view.findViewById(R.id.txtMenuCost)


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.inside_recycler_single_row, parent, false)
            return FAQViewHolder(view)

        }

        override fun getItemCount(): Int {
            return iList.size

        }

        override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
            val inside = iList[position]
            holder.txtSno.text = (position+1).toString()
            holder.txtMenuName.text = "${inside.menName} | ${inside.menCategory}"
            holder.txtMenuCost.text = "₹${inside.menPrice} X ${inside.menQuantity}"
        }
    }
