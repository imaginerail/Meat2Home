package com.example.restaurantalpha.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class OrderItemEntity(
    @PrimaryKey val menId: String,
    @ColumnInfo(name = "menu_name") val menName: String,
    @ColumnInfo(name = "menu_category") val menCategory: String,
    @ColumnInfo(name = "menu_image") val menImage: String,
    @ColumnInfo(name = "menu_price") val menPrice: Int,
    @ColumnInfo(name = "menu_quantity") var menQuantity: Int
)