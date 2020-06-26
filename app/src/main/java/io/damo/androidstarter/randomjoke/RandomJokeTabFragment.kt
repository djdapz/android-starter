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
import io.damo.androidstarter.favorites.Favorite
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
        val favoritesRepo = appComponent.favoritesRepo

        jokeViewModel.joke().observe(this) { jokeData ->
            loadJokeIfNeeded(jokeData, jokeViewModel)
            updateJokeTextView(jokeData)
            updateSwipeRefresh(jokeData)
            updateFavoriteButtonState(jokeData, favoritesRepo, view)
        }

        swipeRefresh.setOnRefreshListener {
            jokeViewModel.loadJoke()
        }

        view.findViewById<Button>(R.id.addToFavorites).setOnClickListener {
            jokeViewModel.jokeText()?.let {
                favoritesRepo.save(it)
            }
        }

        favoritesRepo.favoritesSubscription.observe(this) { favorites: List<Favorite> ->
            updateFavoriteButtonVisibility(jokeViewModel, favorites, view)
        }
    }

    private fun updateFavoriteButtonVisibility(
        jokeViewModel: RandomJokeViewModel,
        favorites: List<Favorite>,
        view: View
    ) {
        jokeViewModel.jokeText()?.let { joke ->
            if (favorites.map { it.joke }.contains(joke)) {
            view.findViewById<Button>(R.id.addToFavorites).isVisible = false
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
            is Error -> false
            is Loaded -> favoritesService.getFavorites()
                .firstOrNull { it.joke == jokeData.data.content } == null
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
