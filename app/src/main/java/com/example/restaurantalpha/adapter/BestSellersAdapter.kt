package com.example.restaurantalpha.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.database.FavouritesEntity
import com.example.restaurantalpha.database.OrderItemEntity
import com.example.restaurantalpha.model.Food
import com.squareup.picasso.Picasso
import kotlin.collections.ArrayList

class BestSellersAdapter(
    val context: Context, var itemList: ArrayList<Food>
) :
    RecyclerView.Adapter<BestSellersAdapter.HomeViewHolder>() {

    var searchFilterList = ArrayList<Food>()
    var totalCost: Int = 0

    var itemSelectedCount: Int = 0

    init {
        searchFilterList = itemList
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_bestsellers_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchFilterList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurants = searchFilterList[position]
        holder.txtMenuName.text = restaurants.name
        holder.txtMenuCost.text = "MRP ₹${restaurants.cost}"
        holder.txtMenuCost1.text = "MRP ₹${restaurants.cost}"
        Picasso.get().load(restaurants.image).into(holder.imgMenu)
        holder.llplusminus.visibility = View.GONE
        //favourites
        val listOfFavourites = GetAllFavAsyncTask(context).execute().get()
        if (listOfFavourites.isNotEmpty() && listOfFavourites.contains(restaurants.id)) {
            holder.btnAddFav.setImageResource(R.drawable.ic_heart)
        } else {
            holder.btnAddFav.setImageResource(R.drawable.ic_favy)
        }
        holder.btnAddFav.setOnClickListener {
            val restaurantEntity = FavouritesEntity(
                restaurants.id,
                restaurants.name,
                restaurants.category,
                restaurants.cost,
                restaurants.image
            )
            if (!DBAsyncTask(context, restaurantEntity, 1).execute().get()) {
                val result = DBAsyncTask(context, restaurantEntity, 2).execute().get()
                //val result = async.get()
                if (result) {
                    Toast.makeText(context, "Item Added To Favourites", Toast.LENGTH_LONG)
                        .show()
                    holder.btnAddFav.setImageResource(R.drawable.ic_heart)
                }
            } else {
                val result = DBAsyncTask(context, restaurantEntity, 3).execute().get()
                if (result) {
                    Toast.makeText(context, "Item Removed from Favourites", Toast.LENGTH_LONG)
                        .show()
                    holder.btnAddFav.setImageResource(R.drawable.ic_favy)

                }
            }
        }


        //orders
        val listOfItems = GetAllItemsAsyncTask(context).execute().get()
        Log.d("fa", listOfItems.toString())
        if (listOfItems.isNotEmpty() && listOfItems.contains(restaurants.id)) {
            val cQ = GetQCById(context, restaurants.id).execute().get()
            holder.llcostadd.visibility = View.GONE
            holder.llplusminus.visibility = View.VISIBLE
            holder.txtquantity.text = "$cQ"
        } else {
            holder.llcostadd.visibility = View.VISIBLE
            holder.llplusminus.visibility = View.GONE
        }

        var orderItemEntity = OrderItemEntity(
            restaurants.id,
            restaurants.name,
            restaurants.category,
            restaurants.image,
            restaurants.cost,
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
                restaurants.id,
                restaurants.name,
                restaurants.category,
                restaurants.image,
                restaurants.cost,
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
                    restaurants.id,
                    restaurants.name,
                    restaurants.category,
                    restaurants.image,
                    restaurants.cost,
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


}

