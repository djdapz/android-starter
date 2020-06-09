package io.damo.androidstarter.randomjoke

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.damo.androidstarter.AppPreferences
import io.damo.androidstarter.backend.JokeApi
import io.damo.androidstarter.support.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RandomJokeViewModel(
    private val jokeApi: JokeApi,
    private val appPreferences: AppPreferences,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val joke = createLiveRemoteData(cachedJoke())

    fun joke(): LiveRemoteData<JokeView> = joke

    fun jokeText(): String? = when (val value = joke.value) {
        is RemoteData.Loaded -> value.data.content
        is RemoteData.NotLoaded -> null
        is RemoteData.Loading -> null
        is RemoteData.Error -> null
        null -> null
    }

    fun loadJoke() =
        viewModelScope.launch {
            joke.loadWith(::fetchJoke)
        }

    private suspend fun fetchJoke(): Result<JokeView> =
        withContext(ioDispatcher) {
            jokeApi
                .getRandomJoke()
                .map {
                    appPreferences.saveJoke(it.joke)
                    JokeView(it.joke)
                }
        }

    private fun cachedJoke(): JokeView? =
        appPreferences.getJoke()?.let { JokeView(it) }
}

data class JokeView(val content: String)
