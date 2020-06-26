package io.damo.androidstarter.instrumentationsupport

import android.app.Application
import io.damo.androidstarter.AppComponent
import io.damo.androidstarter.DefaultAppComponent
import io.damo.androidstarter.ViewModelFactory
import io.damo.androidstarter.backend.JokeApi
import io.damo.androidstarter.favorites.FavoritesRepo
import io.damo.androidstarter.favorites.FavoritesRoomRepo

class TestAppComponent(app: Application) : AppComponent by DefaultAppComponent(app) {


    val testDispatcher = TestDispatcher()

    private val mockServer = startMockServer(testDispatcher)
    private val baseUrl = mockServer.baseUrl()

    override val jokeApi = JokeApi(baseUrl)
    override val favoritesRepo: FavoritesRepo = FavoritesRoomRepo.initialize(app)
    override val viewModelFactory = ViewModelFactory(this)

    fun shutdownServer() {
        mockServer.shutdown()
    }
}
