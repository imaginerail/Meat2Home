package com.example.restaurantalpha.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderItemDao {


    @Insert
    fun insertOrd(orderEntity: OrderItemEntity)

    @Delete
    fun deleteOrd(orderEntity: OrderItemEntity)

    @Query("UPDATE `cart` SET menu_quantity=:c WHERE menId=:ordId")
    fun manipulateQuantity(ordId: String,c:Int):Int

    @Query("SELECT * FROM `cart`")
    fun getAllOrders(): List<OrderItemEntity>

    @Query("SELECT * FROM `cart` WHERE menId=:ordId ")
    fun getOrderById(ordId: String): OrderItemEntity

    @Query("SELECT menu_quantity FROM `cart` WHERE menId=:ordId ")
    fun getQuantityById(ordId: String): Int


    @Query("SELECT SUM(menu_price) FROM `cart`")
    fun getSum(): Int

    @Query("DELETE FROM `cart`")
    fun deleteAll(): Int
}