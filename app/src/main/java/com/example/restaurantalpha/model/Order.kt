package com.example.restaurantalpha.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class Order (
    val orderId:String="",
    val items: ArrayList<HashMap<String,Any>> = ArrayList(),
    val timeStamp: String="",
    val totalCost: Int = 0
)