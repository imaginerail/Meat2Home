package com.example.restaurantalpha.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [OrderItemEntity::class], version = 2)
abstract class OrderItemDatabase : RoomDatabase() {
    abstract fun orderItemDao(): OrderItemDao
}