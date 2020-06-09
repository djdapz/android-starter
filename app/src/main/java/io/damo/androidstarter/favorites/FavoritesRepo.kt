package io.damo.androidstarter.favorites

import android.content.Context
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.damo.androidstarter.AppPreferences
import kotlinx.coroutines.MainScope

class FavoritesRepo(context: Context) : ViewModel() {
    private val appPreferences: AppPreferences = AppPreferences(context)
    private val liveData = MutableLiveData(
        getFavorites()
    )

    fun liveData(): LiveData<List<String>> = liveData

    fun remove(content: String) {
        appPreferences.removeFavorite(content)
        liveData.postValue(appPreferences.getFavorites())
    }

    fun add(content: String) {
        appPreferences.saveFavorite(content)
        liveData.postValue(appPreferences.getFavorites())
    }

    fun getFavorites(): List<String> = appPreferences.getFavorites()

}
