package io.damo.androidstarter

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class AppPreferences(context: Context) {

    val prefsFile = "io.damo.AppPreferences"
    val jokeKey = "Joke"
    private val favoritesKey = "Favorites"
    private val delimiter = "||"

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE)
    }

    fun saveJoke(jokeText: String) =
        sharedPreferences.edit { putString(jokeKey, jokeText) }

    fun saveFavorite(jokeText: String) = sharedPreferences.edit {
        getFavorites()
            .toMutableSet()
            .apply { this.add(jokeText) }
            .joinToString(delimiter)
            .let { putString(favoritesKey, it) }
    }

    fun getFavorites(): List<String> =
        sharedPreferences.getString(favoritesKey, null)
            ?.split(delimiter)
            ?.filter { it.isNotEmpty() } ?: emptyList()

    fun removeFavorite(jokeText: String) = sharedPreferences.edit {
        getFavorites()
            .toMutableSet()
            .apply { this.remove(jokeText) }
            .joinToString(delimiter)
            .let { putString(favoritesKey, it) }
    }

    fun getJoke(): String? =
        sharedPreferences.getString(jokeKey, null)

    fun clear() =
        sharedPreferences.edit { clear() }
}
