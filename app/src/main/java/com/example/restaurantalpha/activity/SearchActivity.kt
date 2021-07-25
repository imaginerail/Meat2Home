package com.example.restaurantalpha.activity

import android.app.AlertDialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.EditText

import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantalpha.R

import com.example.restaurantalpha.adapter.SearchAdapter

import com.example.restaurantalpha.model.Search
import com.example.restaurantalpha.util.ConnectionManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchActivity : AppCompatActivity() {
    lateinit var search: SearchView
    lateinit var searchEditText: EditText
    lateinit var recycleSearch: RecyclerView
    var searchList = arrayListOf<Search>()
    lateinit var searchAdapter: SearchAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        search = findViewById(R.id.searchView)
        recycleSearch = findViewById(R.id.recycleSearch)
        setUpRecycler()
        setupSearch()
    }

    private fun setUpRecycler() {
        val category = arrayOf("Biryani", "Chicken", "Fish", "Mutton")
        if (ConnectionManager().checkConnection(this)) {
            for (i in category.indices) {
                val ref = FirebaseDatabase.getInstance()
                    .getReference("/Menu/${category[i]}")
                ref.addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {
                        //
                    }

                    override fun onDataChange(p0: DataSnapshot) {


                        p0.children.forEach {
                            val srch = it.getValue(Search::class.java)
                            val sl = Search(category[i], srch!!.name, srch.cost, srch.image)
                            searchList.add(sl)
                        }



                        if (this@SearchActivity != null) {
                            searchAdapter =
                                SearchAdapter(
                                    this@SearchActivity,
                                    searchList
                                )
                            val mLayoutManager = LinearLayoutManager(this@SearchActivity)
                            recycleSearch.layoutManager = mLayoutManager
                            recycleSearch.itemAnimator = DefaultItemAnimator()
                            recycleSearch.adapter = searchAdapter
                            recycleSearch.setHasFixedSize(true)
                        }


                    }

                })
            }
        } else {
            checkIfNoInternet()
        }
    }

    private fun checkIfNoInternet() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Failure")
        dialog.setMessage("Internet Connection NOT Found")
        dialog.setPositiveButton("Open Settings") { _, _ ->
            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(settingsIntent)
            finish()
        }
        dialog.setNegativeButton("Exit") { _, _ ->
            ActivityCompat.finishAffinity(this)
        }
        dialog.create()
        dialog.show()
    }

    private fun setupSearch() {
        search.queryHint = "What are you craving for?"
        searchEditText = findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.colorsearch))
        search.setOnQueryTextListener(object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchAdapter.filter.filter(newText)
                return false
            }

        })
    }
}