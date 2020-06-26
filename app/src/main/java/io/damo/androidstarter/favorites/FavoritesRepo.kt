package io.damo.androidstarter.favorites

import androidx.lifecycle.LiveData

interface FavoritesRepo {
    val favoritesSubscription: LiveData<List<Favorite>>
    fun remove(id: Int)
    fun save(joke: String)
    fun getFavorites(): List<Favorite>
}