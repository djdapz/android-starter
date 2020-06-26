package io.damo.androidstarter.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val DATABASE_NAME = "favorites-db"

class FavoritesRoomRepo private constructor(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    FavoritesRepo, ViewModel() {

    private val dao = Room.databaseBuilder(
        context.applicationContext,
        FavoritesDatabase::
        class.java,
        DATABASE_NAME
    )
        .build().favoritesDao()

    companion object {
        private var INSTANCE: FavoritesRoomRepo? = null

        fun initialize(context: Context): FavoritesRoomRepo {
            return INSTANCE ?: FavoritesRoomRepo(context).also { INSTANCE = it }
        }

        fun get(): FavoritesRoomRepo {
            return INSTANCE ?: throw IllegalStateException("Repo must be initialized")
        }
    }

    override val favoritesSubscription: LiveData<List<Favorite>> = dao.getFavorites()


    override fun remove(id: Int) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                dao.remove(id)
            }
        }
    }

    override fun save(joke: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                dao.add(joke)
            }
        }
    }

    override fun getFavorites(): List<Favorite> = favoritesSubscription.value ?: emptyList()
}
