package com.example.restaurantalpha.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.activity.CategoryMainActivity
import com.example.restaurantalpha.model.Food
import com.example.restaurantalpha.model.Search
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(
    val context: Context, var itemList: ArrayList<Search>
) :
    RecyclerView.Adapter<SearchAdapter.HomeViewHolder>(), Filterable {
    var searchFilterList = ArrayList<Search>()

    init {
        searchFilterList = itemList
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContent: LinearLayout = view.findViewById(R.id.llContent)
        val imgResImage: ImageView = view.findViewById(R.id.imgResImage)
        val btnView: Button = view.findViewById(R.id.btnView)
        val txtResName: TextView = view.findViewById(R.id.txtResName)
        val txtResPrice: TextView = view.findViewById(R.id.txtResPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_recycler_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {

        return searchFilterList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val src = itemList[position]
        holder.txtResName.text = src.name
        holder.txtResPrice.text = "${src.category} | ₹${src.cost}"
        Picasso.get().load(src.image).into(holder.imgResImage)

        holder.btnView.setOnClickListener {
            val i = Intent(context, CategoryMainActivity::class.java)
            i.putExtra("tabposition", src.category)
            i.putExtra("itemposition", src.name)
            context.startActivity(i)

        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                if (charString.isEmpty()) {
                    searchFilterList= itemList
                } else {
                    val filteredList: ArrayList<Search> = ArrayList()
                    for (row in itemList) {
                        if (row.name.toLowerCase(Locale.getDefault())
                                .contains(charString) || row.category.toLowerCase(Locale.getDefault())
                                .contains(charString)
                        ) {
                            filteredList.add(row)
                        }
                    }

                    searchFilterList = filteredList

                }
                val filterResults = FilterResults()
                filterResults.values = searchFilterList
                return filterResults
            }


            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                itemList = p1?.values as ArrayList<Search>
                notifyDataSetChanged()
            }

        }
    }
}