package com.example.restaurantalpha.adapter


import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.database.OrderItemEntity
import com.example.restaurantalpha.fragment.CartFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CartAdapter(
    val context: Context,
    var itemList: ArrayList<OrderItemEntity>
) :
    RecyclerView.Adapter<CartAdapter.HomeViewHolder>() {


    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llcart1: LinearLayout = view.findViewById(R.id.llcart1)
        val ibChicken: CircleImageView = view.findViewById(R.id.ibChicken)
        val txtMenuName: TextView = view.findViewById(R.id.txtMenuName)
        val txtMenuCost: TextView = view.findViewById(R.id.txtMenuCost)
        val btnplus: ImageButton = view.findViewById(R.id.btnplus)
        val btnminus: ImageButton = view.findViewById(R.id.btnminus)
        val txtquantity: TextView = view.findViewById(R.id.txtquantity)
        val imgDelete: ImageView = view.findViewById(R.id.imgDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mycart_recycler_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurants = itemList[position]
        Picasso.get().load(restaurants.menImage).into(holder.ibChicken)
        holder.txtMenuName.text = restaurants.menName
        holder.txtMenuCost.text = "${restaurants.menCategory} | ${restaurants.menPrice}"
        holder.txtquantity.text = restaurants.menQuantity.toString()

        holder.imgDelete.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("Confirmation")
            dialog.setMessage("Are you sure you wanna remove this item from your cart?")
            dialog.setPositiveButton("Yes") { _, _ ->
                val result3 = DBAsyncTask2(context, restaurants, 3).execute().get()
                if (result3) {
                    Toast.makeText(
                        context,
                        "${restaurants.menName} has been removed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.llcart1.visibility = View.GONE
                    refresh()
                }
            }
            dialog.setNegativeButton("Cancel") { _, _ ->
                dialog.show().cancel()
            }
            dialog.create()
            dialog.show()

        }

        var orderItemEntity = OrderItemEntity(
            restaurants.menId,
            restaurants.menName,
            restaurants.menCategory,
            restaurants.menImage,
            restaurants.menPrice,
            restaurants.menQuantity
        )

        holder.btnplus.setOnClickListener {
            var currentQuantity = GetQuantityCount(context, orderItemEntity).execute().get()
            orderItemEntity = OrderItemEntity(
                restaurants.menId,
                restaurants.menName,
                restaurants.menCategory,
                restaurants.menImage,
                restaurants.menPrice,
                ++currentQuantity
            )

            val result4 = DBAsyncTask2(context, orderItemEntity, 4).execute().get()
            if (result4) {
                holder.txtquantity.text = "${orderItemEntity.menQuantity}"
                holder.txtMenuCost.text =
                    "${restaurants.menCategory} | ${orderItemEntity.menPrice * orderItemEntity.menQuantity}"
                refresh()
            }
        }

        holder.btnminus.setOnClickListener {
            var currentQuantity = GetQuantityCount(context, orderItemEntity).execute().get()
            if (currentQuantity > 1) {
                orderItemEntity = OrderItemEntity(
                    restaurants.menId,
                    restaurants.menName,
                    restaurants.menCategory,
                    restaurants.menImage,
                    restaurants.menPrice,
                    --currentQuantity
                )
                val result4 = DBAsyncTask2(context, orderItemEntity, 4).execute().get()
                if (result4) {
                    holder.txtquantity.text = "${orderItemEntity.menQuantity}"
                    holder.txtMenuCost.text =
                        "${restaurants.menCategory} | ${orderItemEntity.menPrice * orderItemEntity.menQuantity}"
                    refresh()
                }
            } else {
                val dialog = AlertDialog.Builder(context)
                dialog.setTitle("Confirmation")
                dialog.setMessage("Are you sure you wanna remove this item from your cart?")
                dialog.setPositiveButton("Yes") { _, _ ->
                    val result3 = DBAsyncTask2(context, orderItemEntity, 3).execute().get()
                    if (result3) {
                        Toast.makeText(
                            context,
                            "${restaurants.menName} has been removed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        holder.llcart1.visibility = View.GONE
                        refresh()
                    }
                }
                dialog.setNegativeButton("Cancel") { _, _ ->
                    dialog.show().cancel()
                }
                dialog.create()
                dialog.show()
            }


        }

    }
       private  fun refresh() {
           (context as AppCompatActivity)
               .supportFragmentManager
               .beginTransaction()
               .replace(R.id.frame,CartFragment())
               .addToBackStack(null)
               .commit()


    }
}




