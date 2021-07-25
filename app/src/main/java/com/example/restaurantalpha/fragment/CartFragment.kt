package com.example.restaurantalpha.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi

import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.restaurantalpha.R
import com.example.restaurantalpha.activity.MainActivity
import com.example.restaurantalpha.activity.PhoneVerificationActivity
import com.example.restaurantalpha.adapter.CartAdapter
import com.example.restaurantalpha.database.OrderItemDatabase
import com.example.restaurantalpha.database.OrderItemEntity
import com.example.restaurantalpha.model.Cart
import com.example.restaurantalpha.model.Customer
import com.example.restaurantalpha.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.paytm.pgsdk.PaytmClientCertificate
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class CartFragment : Fragment() {
    lateinit var scrollView: ScrollView
    lateinit var recycleCart: RecyclerView
    lateinit var txtTotalDetails: TextView
    lateinit var txtSupportRider: TextView
    lateinit var btn20: Button
    lateinit var btn30: Button
    lateinit var btn50: Button
    lateinit var txtItemTotal: TextView
    lateinit var txtTaxes: TextView
    lateinit var txtTip: TextView
    lateinit var txtGrandTotal: TextView
    lateinit var numItemTotal: TextView
    lateinit var numTaxes: TextView
    lateinit var numTip: TextView
    lateinit var numGrandTotal: TextView
    lateinit var btnPlaceOrder: Button
    lateinit var txtcleartip: TextView
    private lateinit var cartAdapter: CartAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var noLayout: RelativeLayout
    lateinit var imgEmptyCart: ImageView
    lateinit var btnExploreNow: Button
    lateinit var txtempty: TextView
    lateinit var progressBar: ProgressBar
    var orderList = arrayListOf<OrderItemEntity>()
    var cartList = arrayListOf<Cart>()
    var totalCost = 0
    var tip = 0
    var grandTotal = 0

    val MID = "NFTFHI98922284081341"
    var ORDER_ID = "ORDER0000001"
    val CUST_ID = "CUST00001"
    val INDUSTRY_TYPE_ID = "Retail"
    val CHANNEL_ID = "WAP"
    val TXN_AMOUNT = "1.00"
    val WEBSITE = "WEBSTAGING"
    val CALLBACK_URL = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp"
    var EMAIL = "abc@gmail.com"
    val MOBILE_NO = "7777777777"
    val uid = FirebaseAuth.getInstance().uid
    val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view = inflater.inflate(R.layout.fragment_f_a_q, container, false)
extractEmail()

        btnExploreNow = view.findViewById(R.id.btnExploreNow)
        scrollView = view.findViewById(R.id.scrollView)
        noLayout = view.findViewById(R.id.noLayout)
        imgEmptyCart = view.findViewById(R.id.imgEmptyCart)
        txtempty = view.findViewById(R.id.txtempty)
        recycleCart = view.findViewById(R.id.recycleCart)
        txtTotalDetails = view.findViewById(R.id.txtTotalDetails)
        txtSupportRider = view.findViewById(R.id.txtSupportRider)
        btn20 = view.findViewById(R.id.btn20)
        btn30 = view.findViewById(R.id.btn30)
        btn50 = view.findViewById(R.id.btn50)
        txtItemTotal = view.findViewById(R.id.txtItemTotal)
        txtTaxes = view.findViewById(R.id.txtTaxes)
        txtTip = view.findViewById(R.id.txtTip)
        txtGrandTotal = view.findViewById(R.id.txtGrandTotal)
        numItemTotal = view.findViewById(R.id.numItemTotal)
        numTaxes = view.findViewById(R.id.numTaxes)
        numTip = view.findViewById(R.id.numTip)
        numGrandTotal = view.findViewById(R.id.numGrandTotal)
        btnPlaceOrder = view.findViewById(R.id.btnPlaceOrder)
        txtcleartip = view.findViewById(R.id.txtcleartip)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.GONE



        orderList = MyCart(activity as Context).execute().get() as ArrayList<OrderItemEntity>
        for (i in 0 until orderList.size) {
            totalCost += (orderList[i].menPrice * orderList[i].menQuantity)
        }
        if (orderList.isEmpty()) {
            noLayout.visibility = View.VISIBLE
        } else {
            noLayout.visibility = View.GONE
        }
        btnExploreNow.setOnClickListener {

            startActivity(Intent(context, MainActivity::class.java))
            activity?.finish()
        }

        setUpTotal(tip)

        btn20.setOnClickListener {
            tip += 20
            setUpTotal(tip)
        }
        btn30.setOnClickListener {
            tip += 30
            setUpTotal(tip)
        }
        btn50.setOnClickListener {
            tip += 50
            setUpTotal(tip)
        }
        txtcleartip.setOnClickListener {
            tip = 0
            setUpTotal(tip)
        }
        setupCartRecycler()

        btnPlaceOrder.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            generatecheckSumHash()
        }





        return view
    }

    private fun extractEmail() {
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (!p0.exists() && FirebaseAuth.getInstance().currentUser == null) {
                    startActivity(Intent(activity as Context, PhoneVerificationActivity::class.java))
                }else{
                val user = p0.getValue(Customer::class.java)
                EMAIL = user!!.cemail
                Log.d("catty", EMAIL)
            }
            }


        })
    }


    private fun setUpTotal(tip: Int) {
        numItemTotal.text = "₹$totalCost"
        val taxes = totalCost * 18 / 100
        numTaxes.text = "₹$taxes"
        numTip.text = "₹$tip"
        grandTotal = totalCost + taxes + tip
        numGrandTotal.text = "₹$grandTotal"

    }

    private fun setupCartRecycler() {
        cartAdapter =
            CartAdapter(
                activity as Context,
                orderList
            )
        val mLayoutManager = GridLayoutManager(activity as Context, 2)
        recycleCart.layoutManager = mLayoutManager
        recycleCart.itemAnimator = DefaultItemAnimator()
        recycleCart.adapter = cartAdapter
        recycleCart.setHasFixedSize(true)
    }

    private fun generatecheckSumHash() {
        val random = Random(System.currentTimeMillis())
        ORDER_ID = "Order" + (1 + random.nextInt(2) * 10000) + random.nextInt(10000)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://androaneeq.000webhostapp.com/paytmchecksumgenerator.php"

        if (ConnectionManager().checkConnection(activity as Context)) {

            val params = HashMap<String, String>()


            params["MID"] = MID
            params["ORDER_ID"] = ORDER_ID
            params["CUST_ID"] = uid.toString()
            params["INDUSTRY_TYPE_ID"] = INDUSTRY_TYPE_ID
            params["CHANNEL_ID"] = CHANNEL_ID
            params["TXN_AMOUNT"] = grandTotal.toString()
            params["WEBSITE"] = WEBSITE
            params["CALLBACK_URL"] = CALLBACK_URL
            params["EMAIL"] = EMAIL
            params["MOBILE_NO"] = MOBILE_NO


            val jsonObjectRequest = object : JsonObjectRequest(
                Method.POST, url, JSONObject(params as Map<*, *>), Response.Listener {

                    progressLayout.visibility = View.GONE

                    try {
                        val checksumHash = it.getString("CHECKSUMHASH")
                        if (checksumHash.isNotEmpty()) {
                            startPaytmTransaction(checksumHash)
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            activity as Context,
                            "Some UnExpected Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }


                },
                Response.ErrorListener {

                    if (activity != null)
                        Toast.makeText(
                            activity as Context,
                            "Volley Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()

                }) {

            }
            queue.add(jsonObjectRequest)
        } else {

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

    private fun startPaytmTransaction(checksumHash: String) {

        val service = PaytmPGService.getStagingService();
        val params = HashMap<String, String>();

        // these are mandatory parameters

        params["MID"] = MID
        params["CHECKSUMHASH"] = checksumHash
        params["ORDER_ID"] = ORDER_ID
        params["CUST_ID"] = uid.toString()
        params["INDUSTRY_TYPE_ID"] = INDUSTRY_TYPE_ID
        params["CHANNEL_ID"] = CHANNEL_ID
        params["TXN_AMOUNT"] = grandTotal.toString()
        params["WEBSITE"] = WEBSITE
        params["CALLBACK_URL"] = CALLBACK_URL
        params["EMAIL"] = EMAIL
        params["MOBILE_NO"] = MOBILE_NO

        val order = PaytmOrder(params);

        val merchant = PaytmClientCertificate(
            "https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
            "https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp"
        )

        service.initialize(order, merchant)

        service.startPaymentTransaction(activity as Context, true, true,
            object : PaytmPaymentTransactionCallback {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onTransactionResponse(inResponse: Bundle?) {
                    Toast.makeText(
                        activity as Context,
                        "Payment Transaction is successful ",
                        Toast.LENGTH_LONG
                    ).show()
                    val r1 = FirebaseDatabase.getInstance().getReference("Orders/$uid/$ORDER_ID")
                    r1.child("orderId").setValue(ORDER_ID)
                    val now = LocalDateTime.now()
                    val datime = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                    r1.child("timeStamp").setValue(datime)
                    r1.child("totalCost").setValue(grandTotal)
                    val r2 = r1.child("items")
                    for (i in 0 until orderList.size) {
                        r2.child("$i").setValue(orderList[i])
                    }

                    startActivity(Intent(context, MainActivity::class.java))
                    ClearAll(activity as Context).execute()
                    activity?.finishAffinity()
                }

                override fun clientAuthenticationFailed(inErrorMessage: String?) {
                    //
                }

                override fun someUIErrorOccurred(inErrorMessage: String?) {
                    //
                }

                override fun onTransactionCancel(inErrorMessage: String?, inResponse: Bundle?) {
                    Log.d("LOG", "Payment Transaction Failed $inErrorMessage");
                    Toast.makeText(
                        activity as Context,
                        "Payment Transaction Failed ",
                        Toast.LENGTH_LONG
                    ).show();
                }

                override fun networkNotAvailable() {
                    //
                }

                override fun onErrorLoadingWebPage(
                    iniErrorCode: Int,
                    inErrorMessage: String?,
                    inFailingUrl: String?
                ) {
                    //
                }

                override fun onBackPressedCancelTransaction() {
                    //
                }


            })


    }

}

class MyCart(val context: Context) : AsyncTask<Void, Void, List<OrderItemEntity>>() {
    override fun doInBackground(vararg p0: Void?): List<OrderItemEntity> {
        return Room.databaseBuilder(context, OrderItemDatabase::class.java, "ord-db").build()
            .orderItemDao().getAllOrders()
    }


}

class ClearAll(val context: Context) : AsyncTask<Void, Void, Int>() {
    override fun doInBackground(vararg p0: Void?): Int {
        val db = Room.databaseBuilder(context, OrderItemDatabase::class.java, "ord-db").build()
        return db.orderItemDao().deleteAll()
    }

}