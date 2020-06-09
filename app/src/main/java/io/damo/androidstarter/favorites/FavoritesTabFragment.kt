package io.damo.androidstarter.favorites

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.damo.androidstarter.R
import io.damo.androidstarter.appComponent
import kotlinx.android.synthetic.main.fragment_favorites.*


class FavoritesTabFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_favorites, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let(::setupView)
    }

    private fun setupView(activity: Activity) {
        activity.title = getString(R.string.favorites_title)

        favoritesList.adapter = FavoritesListAdapter(requireContext())
    }
}
