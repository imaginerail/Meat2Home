package com.example.restaurantalpha.adapter


import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.database.FavouritesEntity
import com.example.restaurantalpha.database.OrderItemEntity
import com.example.restaurantalpha.fragment.CartFragment
import com.example.restaurantalpha.fragment.FavouriteFragment
import com.squareup.picasso.Picasso

class FavouritesAdapter(
    val context: Context,
    var lnList: ArrayList<FavouritesEntity>
) :
    RecyclerView.Adapter<FavouritesAdapter.SLViewHolder>() {



    class SLViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llplusminus: LinearLayout = view.findViewById(R.id.llplusminus)
        val llcostadd: LinearLayout = view.findViewById(R.id.llcostadd)
        val btnAddFav: ImageView = view.findViewById(R.id.btnAddFav)
        val btnAddtoCart: Button = view.findViewById(R.id.btnAddtoCart)
        val btnplus: ImageButton = view.findViewById(R.id.btnplus)
        val btnminus: ImageButton = view.findViewById(R.id.btnminus)
        val txtquantity: TextView = view.findViewById(R.id.txtquantity)
        val txtMenuCost: TextView = view.findViewById(R.id.txtMenuCost)
        val txtMenuCost1: TextView = view.findViewById(R.id.txtMenuCost1)
        val imgMenu: ImageView = view.findViewById(R.id.imgMenu)
        val txtMenuName: TextView = view.findViewById(R.id.txtMenuName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SLViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourites_recycler_single_row, parent, false)
        return SLViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lnList.size

    }

    override fun onBindViewHolder(holder: SLViewHolder, position: Int) {
        val restaurants = lnList[position]
        holder.txtMenuName.text = restaurants.resName
        holder.txtMenuCost.text = "MRP ₹${restaurants.resPrice}"
        holder.txtMenuCost1.text = "MRP ₹${restaurants.resPrice}"
        Picasso.get().load(restaurants.resImage).into(holder.imgMenu)
        holder.llplusminus.visibility = View.GONE
        holder.btnAddFav.setImageResource(R.drawable.ic_heart)
        holder.btnAddFav.setOnClickListener {

            val result = DBAsyncTask(context, restaurants, 3).execute().get()
                if (result) {
                    Toast.makeText(context, "Restaurant Removed from Favourites", Toast.LENGTH_LONG)
                        .show()
                    holder.btnAddFav.setImageResource(R.drawable.ic_favy)
                    refresh()

                }
            }

        val listOfItems = GetAllItemsAsyncTask(context).execute().get()
        Log.d("fa",listOfItems.toString())
        if (listOfItems.isNotEmpty() && listOfItems.contains(restaurants.resId)) {
            val cQ = GetQCById(context, restaurants.resId).execute().get()
            holder.llcostadd.visibility = View.GONE
            holder.llplusminus.visibility = View.VISIBLE
            holder.txtquantity.text = "$cQ"
        } else {
            holder.llcostadd.visibility = View.VISIBLE
            holder.llplusminus.visibility = View.GONE
        }

        var orderItemEntity = OrderItemEntity(
            restaurants.resId,
            restaurants.resName,
            restaurants.resCategory,
            restaurants.resImage,
            restaurants.resPrice,
            1
        )

        holder.btnAddtoCart.setOnClickListener {
            if (!DBAsyncTask2(context, orderItemEntity, 1).execute().get()) {
                val result2 = DBAsyncTask2(context, orderItemEntity, 2).execute().get()

                if (result2) {
                    holder.llcostadd.visibility = View.GONE
                    holder.llplusminus.visibility = View.VISIBLE
                    holder.txtquantity.text = "${orderItemEntity.menQuantity}"
                }
            }
        }

        holder.btnplus.setOnClickListener {
            var currentQuantity = GetQuantityCount(context, orderItemEntity).execute().get()
            orderItemEntity = OrderItemEntity(
                restaurants.resId,
                restaurants.resName,
                restaurants.resCategory,
                restaurants.resImage,
                restaurants.resPrice,
                ++currentQuantity
            )

            val result4 = DBAsyncTask2(context, orderItemEntity, 4).execute().get()
            if (result4) {
                holder.txtquantity.text = "${orderItemEntity.menQuantity}"
            }
        }

        holder.btnminus.setOnClickListener {
            var currentQuantity = GetQuantityCount(context, orderItemEntity).execute().get()
            if (currentQuantity > 1) {
                orderItemEntity = OrderItemEntity(
                    restaurants.resId,
                    restaurants.resName,
                    restaurants.resCategory,
                    restaurants.resImage,
                    restaurants.resPrice,
                    --currentQuantity
                )
                val result4 = DBAsyncTask2(context, orderItemEntity, 4).execute().get()
                if (result4) {
                    holder.txtquantity.text = "${orderItemEntity.menQuantity}"
                }
            } else {
                val result3 = DBAsyncTask2(context, orderItemEntity, 3).execute().get()
                if (result3) {
                    holder.llplusminus.visibility = View.GONE
                    holder.llcostadd.visibility = View.VISIBLE
                }
            }


        }

        }
    private  fun refresh() {
        (context as AppCompatActivity)
            .supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, FavouriteFragment())
            .addToBackStack(null)
            .commit()


    }
}
