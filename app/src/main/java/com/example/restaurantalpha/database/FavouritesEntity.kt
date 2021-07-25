package com.example.restaurantalpha.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favourites")
class FavouritesEntity (
    @PrimaryKey val resId: String,
    @ColumnInfo(name = "res_name") val resName: String,
    @ColumnInfo(name = "res_category") val resCategory: String,
    @ColumnInfo(name = "res_price") val resPrice: Int,
    @ColumnInfo(name = "res_image") val resImage: String
)