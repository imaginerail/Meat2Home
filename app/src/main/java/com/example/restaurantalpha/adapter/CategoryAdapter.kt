package com.example.restaurantalpha.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.activity.CategoryMainActivity
import com.example.restaurantalpha.model.Category
import com.example.restaurantalpha.model.FAQitems
import com.squareup.picasso.Picasso

class CategoryAdapter(val context: Context, val cList: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.FAQViewHolder>() {
    class FAQViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llcart1: LinearLayout = view.findViewById(R.id.llcart1)
        val imgCat: ImageView = view.findViewById(R.id.imgCat)
        val txtMenuName: TextView = view.findViewById(R.id.txtMenuName)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_recycler_single_row, parent, false)
        return FAQViewHolder(view)

    }

    override fun getItemCount(): Int {
        return cList.size

    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val c = cList[position]
        Picasso.get().load(c.image).into(holder.imgCat)
        holder.txtMenuName.text = c.name
        holder.llcart1.setOnClickListener {
            val i = Intent(context, CategoryMainActivity::class.java)
            i.putExtra("tabposition", c.name)
            context.startActivity(i)

        }
    }
}