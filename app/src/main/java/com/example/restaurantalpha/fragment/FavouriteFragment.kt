package com.example.restaurantalpha.fragment


import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.restaurantalpha.R
import com.example.restaurantalpha.activity.MainActivity
import com.example.restaurantalpha.adapter.FavouritesAdapter
import com.example.restaurantalpha.database.FavouritesDatabase
import com.example.restaurantalpha.database.FavouritesEntity
//import kotlinx.android.synthetic.main.fragment_f_a_q.*


class FavouriteFragment : Fragment() {
    lateinit var noLayout: RelativeLayout
    lateinit var imgEmptyFavourites: ImageView
    lateinit var txtempty: TextView
    lateinit var recycleFav: RecyclerView
    lateinit var btnExploreNow: Button
    var favList = arrayListOf<FavouritesEntity>()
    lateinit var favouritesAdapter: FavouritesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)
        recycleFav = view.findViewById(R.id.recyclefav)
        noLayout = view.findViewById(R.id.noLayout)
        imgEmptyFavourites = view.findViewById(R.id.imgEmptyFavourites)
        txtempty = view.findViewById(R.id.txtempty)
        btnExploreNow = view.findViewById(R.id.btnExploreNow)
        btnExploreNow.setOnClickListener {


            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()


        }
        setUpRecycler()
        return view
    }


    private fun setUpRecycler() {
        favList =
            RetrieveFavourites(activity as Context).execute().get() as ArrayList<FavouritesEntity>

        if (favList.isEmpty()) noLayout.visibility = View.VISIBLE
        else noLayout.visibility = View.GONE

        if (activity != null) {
            favouritesAdapter =
                FavouritesAdapter(
                    activity as Context,
                    favList

                )
            val mLayoutManager =
                LinearLayoutManager(activity as Context)
            recycleFav.layoutManager = mLayoutManager
            recycleFav.itemAnimator = DefaultItemAnimator()
            recycleFav.adapter = favouritesAdapter
            recycleFav.setHasFixedSize(true)
        }

    }
}

class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<FavouritesEntity>>() {
    override fun doInBackground(vararg p0: Void?): List<FavouritesEntity> {
        val db = Room.databaseBuilder(context, FavouritesDatabase::class.java, "res-db").build()
        return db.restaurantDao().getAllRestaurants()
    }

}