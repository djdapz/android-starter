package io.damo.androidstarter.favorites

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Favorite::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
}
