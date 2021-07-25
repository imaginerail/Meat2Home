package com.example.restaurantalpha.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
@Dao
interface FavouritesDao {
    @Insert
    fun insertRes(restaurantEntity: FavouritesEntity)

    @Delete
    fun deleteRes(restaurantEntity: FavouritesEntity)

    @Query("SELECT * FROM favourites")
    fun getAllRestaurants(): List<FavouritesEntity>

    @Query("SELECT * FROM favourites WHERE resId=:resId ")
    fun getRestaurantById(resId: String): FavouritesEntity

    @Query("SELECT res_name FROM favourites")
    fun getRestaurantByName(): String
}