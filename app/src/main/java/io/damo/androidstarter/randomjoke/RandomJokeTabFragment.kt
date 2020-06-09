package io.damo.androidstarter.randomjoke

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.get
import io.damo.androidstarter.R
import io.damo.androidstarter.activityViewModelProvider
import io.damo.androidstarter.appComponent
import io.damo.androidstarter.favorites.FavoritesRepo
import io.damo.androidstarter.support.RemoteData
import io.damo.androidstarter.support.RemoteData.Error
import io.damo.androidstarter.support.RemoteData.Loaded
import io.damo.androidstarter.support.RemoteData.Loading
import io.damo.androidstarter.support.RemoteData.NotLoaded
import io.damo.androidstarter.support.observe
import kotlinx.android.synthetic.main.fragment_random_joke_tab.jokeTextView
import kotlinx.android.synthetic.main.fragment_random_joke_tab.swipeRefresh

class RandomJokeTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_random_joke_tab, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.title = getString(R.string.random_title)

        val jokeViewModel = activityViewModelProvider.get<RandomJokeViewModel>()
        val favoritesService = appComponent.favoritesRepo

        jokeViewModel.joke().observe(this) { jokeData ->
            loadJokeIfNeeded(jokeData, jokeViewModel)
            updateJokeTextView(jokeData)
            updateSwipeRefresh(jokeData)
            updateFavoriteButtonState(jokeData, favoritesService, view)
        }

        swipeRefresh.setOnRefreshListener {
            jokeViewModel.loadJoke()
        }

        view.findViewById<Button>(R.id.addToFavorites).setOnClickListener {
            jokeViewModel.jokeText()?.let { favoritesService.add(it) }
        }

        favoritesService.liveData().observe(this) { favorites ->
            jokeViewModel.jokeText()?.let {
                if (favorites.contains(it)) {
                    view.findViewById<Button>(R.id.addToFavorites).isVisible = false
                }
            }
        }
    }

    private fun updateFavoriteButtonState(
        jokeData: RemoteData<JokeView>,
        favoritesService: FavoritesRepo,
        view: View
    ) {
        val isVisible: Boolean = when (jokeData) {
            is NotLoaded -> false
            is Loading -> false
            is Loaded -> !favoritesService.getFavorites().contains(jokeData.data.content)
            is Error -> false
        }
        view.findViewById<Button>(R.id.addToFavorites).isVisible = isVisible

    }

    private fun updateSwipeRefresh(jokeData: RemoteData<JokeView>) {
        if (jokeData is Loading) return
        swipeRefresh.isRefreshing = false
    }

    private fun updateJokeTextView(jokeData: RemoteData<JokeView>) {
        jokeTextView.text =
            when (jokeData) {
                is NotLoaded -> ""
                is Loading -> context?.getString(R.string.loading) ?: ""
                is Loaded -> jokeData.data.content
                is Error -> jokeData.explanation.message
            }
    }

    private fun loadJokeIfNeeded(jokeData: RemoteData<JokeView>, viewModel: RandomJokeViewModel) {
        if (jokeData is NotLoaded)
            viewModel.loadJoke()
    }
}
