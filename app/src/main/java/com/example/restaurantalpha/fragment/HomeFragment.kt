package com.example.restaurantalpha.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R
import com.example.restaurantalpha.activity.CategoryMainActivity
import com.example.restaurantalpha.activity.SearchActivity
import com.example.restaurantalpha.adapter.*
import com.example.restaurantalpha.model.Category
import com.example.restaurantalpha.model.Food
import com.example.restaurantalpha.model.SliderItem
import com.example.restaurantalpha.model.TrendingNow
import com.example.restaurantalpha.util.ConnectionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment() {
    lateinit var searchView: SearchView
    lateinit var ibfastdelivery: CircleImageView
    lateinit var ibbestdeals: CircleImageView
    lateinit var ibhalal: CircleImageView
    lateinit var txtFastdelivery: TextView
    lateinit var txtBestDeals: TextView
    lateinit var txthalal: TextView
    lateinit var ibChicken: CircleImageView
    lateinit var ibMutton: CircleImageView
    lateinit var ibBeef: CircleImageView
    lateinit var ibRabbit: CircleImageView
    lateinit var ibFish: CircleImageView
    lateinit var imageSlider: SliderView
    private lateinit var sliderAdapter: SliderAdapter
    lateinit var sliderProgressLayout: RelativeLayout
    lateinit var sliderProgressBar: ProgressBar
    lateinit var txtbestsellers: TextView
    lateinit var recycleBestSellers: RecyclerView
    var sliderItemList = ArrayList<SliderItem>()
    lateinit var bestSellersAdapter: BestSellersAdapter
    lateinit var trendingNowAdapter: TrendingNowAdapter
    lateinit var txtTrendingNow: TextView
    lateinit var txtExploreByCategory: TextView
    lateinit var recycleTN: RecyclerView
    lateinit var recycleEBC: RecyclerView
    var cList = arrayListOf<Category>()
    lateinit var categoryAdapter: CategoryAdapter
    var tnList = arrayListOf<TrendingNow>()
    var itemList = arrayListOf<Food>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)


        ibfastdelivery = view.findViewById(R.id.ibfastdelivery)
        ibbestdeals = view.findViewById(R.id.ibbestdeals)
        ibhalal = view.findViewById(R.id.ibhalal)
        txtFastdelivery = view.findViewById(R.id.txtFastdelivery)
        txtBestDeals = view.findViewById(R.id.txtBestDeals)
        txthalal = view.findViewById(R.id.txthalal)
        //
        searchView = view.findViewById(R.id.searchView)
        ibChicken = view.findViewById(R.id.ibChicken)
        ibMutton = view.findViewById(R.id.ibMutton)
        ibBeef = view.findViewById(R.id.ibBeef)
        ibRabbit = view.findViewById(R.id.ibRabbit)
        ibFish = view.findViewById(R.id.ibFish)
        imageSlider = view.findViewById(R.id.imageSlider)
        sliderProgressLayout = view.findViewById(R.id.sliderProgressLayout)
        sliderProgressBar = view.findViewById(R.id.sliderProgressBar)
        sliderProgressLayout.visibility = View.VISIBLE
        setUpSlider()
        setUpBestSellers(view)
        setUpTrendingNow(view)
        setUpExploreCategories(view)
        ibChicken.setOnClickListener {
            val i = Intent(activity as Context, CategoryMainActivity::class.java)
            i.putExtra("tabposition", "Chicken")
            startActivity(i)
        }
        ibMutton.setOnClickListener {
            val i = Intent(activity as Context, CategoryMainActivity::class.java)
            i.putExtra("tabposition", "Mutton")
            startActivity(i)
        }
        ibBeef.setOnClickListener {
            val i = Intent(activity as Context, CategoryMainActivity::class.java)
            i.putExtra("tabposition", "Biryani")
            startActivity(i)
        }
        ibFish.setOnClickListener {
            val i = Intent(activity as Context, CategoryMainActivity::class.java)
            i.putExtra("tabposition", "Fish")
            startActivity(i)
        }
        ibRabbit.setOnClickListener {
            val i = Intent(activity as Context, CategoryMainActivity::class.java)
            i.putExtra("tabposition", "Rabbit")
            startActivity(i)
        }
        searchView.setOnClickListener {
            startActivity(Intent(activity as Context, SearchActivity::class.java))
        }





        return view
    }

    private fun setUpExploreCategories(view: View) {

        txtExploreByCategory = view.findViewById(R.id.txtExploreByCategory)
        recycleEBC = view.findViewById(R.id.recycleEBC)

        if (ConnectionManager().checkConnection(activity as Context)) {
            val ref = FirebaseDatabase.getInstance()
                .getReference("/Categories")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        val category = it.getValue(Category::class.java)
                        cList.add(category!!)
                    }



                    if (activity != null) {
                        categoryAdapter =
                            CategoryAdapter(
                                activity as Context,
                                cList
                            )
                        val mLayoutManager = GridLayoutManager(activity as Context, 2)
                        recycleEBC.isNestedScrollingEnabled = false
                        recycleEBC.layoutManager = mLayoutManager
                        recycleEBC.itemAnimator = DefaultItemAnimator()
                        recycleEBC.adapter = categoryAdapter
                        recycleEBC.setHasFixedSize(true)
                    }


                }

            })
        } else {
            checkIfNoInternet()
        }
    }


    private fun setUpBestSellers(view: View) {
        txtbestsellers = view.findViewById(R.id.txtbestsellers)
        recycleBestSellers = view.findViewById(R.id.recycleBestSellers)

        if (ConnectionManager().checkConnection(activity as Context)) {
            val ref = FirebaseDatabase.getInstance()
                .getReference("/Best Sellers")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {


                    p0.children.forEach {
                        val food = it.getValue(Food::class.java)
                        itemList.add(food!!)
                    }



                    if (activity != null) {
                        bestSellersAdapter =
                            BestSellersAdapter(
                                activity as Context,
                                itemList
                            )
                        val mLayoutManager = LinearLayoutManager(
                            activity as Context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        recycleBestSellers.layoutManager = mLayoutManager
                        recycleBestSellers.itemAnimator = DefaultItemAnimator()
                        recycleBestSellers.adapter = bestSellersAdapter
                        recycleBestSellers.setHasFixedSize(true)
                    }


                }

            })
        } else {
            checkIfNoInternet()
        }
    }


    private fun setUpTrendingNow(view: View) {
        txtTrendingNow = view.findViewById(R.id.txtTrendingNow)
        recycleTN = view.findViewById(R.id.recycleTN)

        if (ConnectionManager().checkConnection(activity as Context)) {
            val ref = FirebaseDatabase.getInstance()
                .getReference("/Trending Now")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {


                    p0.children.forEach {
                        val trnw = it.getValue(TrendingNow::class.java)
                        tnList.add(trnw!!)
                    }



                    if (activity != null) {
                        trendingNowAdapter =
                            TrendingNowAdapter(
                                activity as Context,
                                tnList
                            )
                        val mLayoutManager = LinearLayoutManager(
                            activity as Context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        recycleTN.layoutManager = mLayoutManager
                        recycleTN.itemAnimator = DefaultItemAnimator()
                        recycleTN.adapter = trendingNowAdapter
                        recycleTN.setHasFixedSize(true)
                    }


                }

            })
        } else {
            checkIfNoInternet()
        }
    }

    private fun setUpSlider() {
        if (ConnectionManager().checkConnection(activity as Context)) {
            val ref = FirebaseDatabase.getInstance().getReference("/Slider")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(p0: DatabaseError) {
                    //
                }

                override fun onDataChange(p0: DataSnapshot) {

                    sliderProgressLayout.visibility = View.GONE

                    p0.children.forEach {
                        val slider = it.getValue(SliderItem::class.java)
                        sliderItemList.add(slider!!)
                    }
                    Log.d("sil", sliderItemList.toString())


                    if (activity != null) {
                        sliderAdapter = SliderAdapter(
                            activity as Context,
                            sliderItemList
                        )
                        imageSlider.setSliderAdapter(sliderAdapter)
                    }
                    imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType.
                    // :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    imageSlider.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH;
                    imageSlider.indicatorSelectedColor = Color.WHITE;
                    imageSlider.indicatorUnselectedColor = Color.GRAY;
                    imageSlider.scrollTimeInSec = 4; //set scroll delay in seconds :
                    imageSlider.startAutoCycle();


                }

            })
        } else {
            checkIfNoInternet()
        }
    }

    private fun checkIfNoInternet() {
        val dialog = AlertDialog.Builder(activity as Context)
        dialog.setTitle("Failure")
        dialog.setMessage("Internet Connection NOT Found")
        dialog.setPositiveButton("Open Settings") { _, _ ->
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
            activity?.finish()
        }
        dialog.setNegativeButton("Exit") { _, _ ->
            ActivityCompat.finishAffinity(activity as Activity)
        }
        dialog.create()
        dialog.show()
    }
}


