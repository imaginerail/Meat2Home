package com.example.restaurantalpha.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.model.SliderItem
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso


class SliderAdapter(
    val context: Context,
    var itemList: ArrayList<SliderItem>
) :
    SliderViewAdapter<SliderAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : SliderViewAdapter.ViewHolder(view) {
        val imgMenu: ImageView = view.findViewById(R.id.imgMenu)


    }

    override fun onCreateViewHolder(parent: ViewGroup?): HomeViewHolder {
        val view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.slider_recycler_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(viewHolder: HomeViewHolder?, position: Int) {
        val si = itemList[position]
        Picasso.get().load(si.sliderItem)
            .error(R.drawable.site_icon).into(viewHolder!!.imgMenu)
    }

    fun renewItems(sliderItems: ArrayList<SliderItem>) {
        this.itemList = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        this.itemList.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: SliderItem) {
        this.itemList.add(sliderItem)
        notifyDataSetChanged()
    }
}