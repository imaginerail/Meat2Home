package com.example.restaurantalpha.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavouritesEntity::class], version = 2)
abstract class FavouritesDatabase : RoomDatabase() {
    abstract fun restaurantDao(): FavouritesDao
}