package io.damo.androidstarter.favorites

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FavoritesDao {

    @Query("DELETE FROM favorite WHERE id=(:id)")
    fun remove(id: Int)

    @Query("INSERT INTO favorite (joke) VALUES (:joke)")
    fun add(joke: String)

    @Query("SELECT * from favorite")
    fun getFavorites(): LiveData<List<Favorite>>

}
