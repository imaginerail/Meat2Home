package com.example.restaurantalpha.adapter

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.restaurantalpha.R
import com.example.restaurantalpha.database.FavouritesDatabase
import com.example.restaurantalpha.database.FavouritesEntity
import com.example.restaurantalpha.database.OrderItemDatabase
import com.example.restaurantalpha.database.OrderItemEntity
import com.example.restaurantalpha.model.Food
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class FoodAdapter(
    val context: Context, var itemList: ArrayList<Food>
) :
    RecyclerView.Adapter<FoodAdapter.HomeViewHolder>(), Filterable {

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
            .inflate(R.layout.home_recycler_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchFilterList.size
        //return itemList.size
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
        Log.d("fa",listOfItems.toString())
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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                searchFilterList = if (charString.isEmpty()) {
                    itemList
                } else {
                    val filteredList: ArrayList<Food> = ArrayList()

                    for (row in itemList) {
                        if (row.name.toLowerCase(Locale.getDefault())
                                .contains(charString) || row.name.contains(charString)
                        ) {
                            filteredList.add(row)
                        }
                    }

                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchFilterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                searchFilterList = p1?.values as ArrayList<Food>
                notifyDataSetChanged()
            }

        }
    }


}

//Favourites
class DBAsyncTask(
    val context: Context,
    private val restaurantEntity: FavouritesEntity,
    private val mode: Int
) :
    AsyncTask<Void, Void, Boolean>() {

    private val db = Room.databaseBuilder(context, FavouritesDatabase::class.java, "res-db").build()

    override fun doInBackground(vararg p0: Void?): Boolean {
        when (mode) {
            1 -> {
                val res: FavouritesEntity? =
                    db.restaurantDao().getRestaurantById(restaurantEntity.resId.toString())
                db.close()
                return res != null
            }
            2 -> {
                db.restaurantDao().insertRes(restaurantEntity)
                db.close()
                return true
            }
            3 -> {
                db.restaurantDao().deleteRes(restaurantEntity)
                db.close()
                return true
            }
        }

        return false
    }


}

class GetAllFavAsyncTask(context: Context) : AsyncTask<Void, Void, List<String>>() {

    private val db = Room.databaseBuilder(context, FavouritesDatabase::class.java, "res-db").build()
    override fun doInBackground(vararg params: Void?): List<String> {

        val list = db.restaurantDao().getAllRestaurants()
        val listOfIds = arrayListOf<String>()
        for (i in list) {
            listOfIds.add(i.resId.toString())
        }
        return listOfIds
    }
}

//orders
class DBAsyncTask2(
    val context: Context,
    private val orderEntity: OrderItemEntity,
    private val mode: Int
) :
    AsyncTask<Void, Void, Boolean>() {
    /* mode1=check the db if the order is placed or not
    mode2= insert the order
    mode3= delete the order
     */
    private val db = Room.databaseBuilder(context, OrderItemDatabase::class.java, "ord-db").build()

    override fun doInBackground(vararg p0: Void?): Boolean {
        when (mode) {
            1 -> {
                val ord: OrderItemEntity? =
                    db.orderItemDao().getOrderById(orderEntity.menId.toString())
                db.close()
                return ord != null
            }
            2 -> {
                db.orderItemDao().insertOrd(orderEntity)
                db.close()
                return true
            }
            3 -> {
                db.orderItemDao().deleteOrd(orderEntity)
                db.close()
                return true
            }
            4 -> {
                return db.orderItemDao()
                    .manipulateQuantity(orderEntity.menId.toString(), orderEntity.menQuantity) > 0
            }
        }

        return false
    }

}

class GetAllItemsAsyncTask(val context: Context) : AsyncTask<Void, Void, ArrayList<String>>() {


    override fun doInBackground(vararg params: Void?): ArrayList<String> {
        val db = Room.databaseBuilder(context, OrderItemDatabase::class.java, "ord-db").build()
        val list = db.orderItemDao().getAllOrders()
        val listOfIds = arrayListOf<String>()
        for (i in list) {
            listOfIds.add(i.menId)
        }
        return listOfIds
    }
}

class GetQuantityCount(val context: Context, private val orderEntity: OrderItemEntity) :
    AsyncTask<Void, Void, Int>() {
    override fun doInBackground(vararg p0: Void?): Int {
        val db = Room.databaseBuilder(context, OrderItemDatabase::class.java, "ord-db").build()
        val qua = db.orderItemDao().getQuantityById(orderEntity.menId.toString())
        db.close()
        return qua
    }
}
class GetQCById(val context: Context,val id:String):AsyncTask<Void, Void, Int>(){
    override fun doInBackground(vararg p0: Void?): Int {
    val db = Room.databaseBuilder(context, OrderItemDatabase::class.java, "ord-db").build()
    val q = db.orderItemDao().getQuantityById(id)
    db.close()
    return q
    }
}