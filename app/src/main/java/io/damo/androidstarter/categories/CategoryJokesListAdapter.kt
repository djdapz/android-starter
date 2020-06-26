package io.damo.androidstarter.categories

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import io.damo.androidstarter.R
import io.damo.androidstarter.categories.CategoryJokesListAdapter.Cell.ErrorCell
import io.damo.androidstarter.categories.CategoryJokesListAdapter.Cell.LoadedCell
import io.damo.androidstarter.categories.CategoryJokesListAdapter.Cell.LoadingCell
import io.damo.androidstarter.categories.CategoryJokesListAdapter.Cell.NotLoadedCell
import io.damo.androidstarter.favorites.Favorite
import io.damo.androidstarter.favorites.FavoritesRepo
import io.damo.androidstarter.randomjoke.JokeView
import io.damo.androidstarter.support.Explanation
import io.damo.androidstarter.support.RemoteData
import io.damo.androidstarter.support.RemoteData.Error
import io.damo.androidstarter.support.RemoteData.Loaded
import io.damo.androidstarter.support.RemoteData.Loading
import io.damo.androidstarter.support.RemoteData.NotLoaded
import io.damo.androidstarter.support.observe

class CategoryJokesListAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val favoritesRepo: FavoritesRepo
) : BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)

    var remoteData: RemoteData<List<JokeView>> = NotLoaded()
        set(newData) {
            field = newData
            notifyDataSetChanged()
        }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(R.layout.cell_category_joke, parent, false)
        val viewHolder = ViewHolder(context, view)

        val cell = getCell(position)

        viewHolder.updateWithCell(cell)

        when (cell) {
            NotLoadedCell, LoadingCell, is ErrorCell -> Unit
            is LoadedCell -> setupButtonListeners(viewHolder, cell.view.content)
        }

        return view
    }

    private fun setupButtonListeners(viewHolder: ViewHolder, text: String) {
        val favoritesList = favoritesRepo.getFavorites()
        val canFavorite = !favoritesList.hasJoke(text)
        if (canFavorite) {
            viewHolder.button.isVisible = true
            viewHolder.button.setOnClickListener { favoritesRepo.save(text) }
            favoritesRepo.favoritesSubscription.observe(lifecycleOwner, {
                if (it.hasJoke(text)) {
                    viewHolder.button.isVisible = false
                }
            })
        }
    }

    fun List<Favorite>.hasJoke(joke: String) = map { it.joke }.contains(joke)

    override fun getItem(position: Int): Any =
        getCell(position)

    private fun getCell(position: Int): Cell {
        return when (val d = remoteData) {
            is NotLoaded -> NotLoadedCell
            is Loading -> LoadingCell
            is Loaded -> LoadedCell(d.data[position])
            is Error -> ErrorCell(d.explanation)
        }
    }

    private val notLoadedCellId = -1L
    private val loadingCellId = -2L
    private val errorCellId = -3L

    override fun getItemId(position: Int): Long =
        when (remoteData) {
            is NotLoaded -> notLoadedCellId
            is Loading -> loadingCellId
            is Loaded -> position.toLong()
            is Error -> errorCellId
        }

    override fun getCount(): Int =
        when (val d = remoteData) {
            is NotLoaded -> 0
            is Loading -> 1
            is Loaded -> d.data.size
            is Error -> 1
        }

    class ViewHolder(
        private val context: Context,
        view: View
    ) {

        private val textView = view.findViewById<TextView>(R.id.jokeInCategoryList)
        val button = view.findViewById<ImageButton>(R.id.favoriteJoke)

        fun updateWithCell(cell: Cell) =
            when (cell) {
                NotLoadedCell -> Unit
                LoadingCell -> textView.text = context.getString(R.string.loading)
                is LoadedCell -> textView.text = cell.view.content
                is ErrorCell -> textView.text = context.getString(R.string.generic_error)
            }
    }

    sealed class Cell {
        object NotLoadedCell : Cell()
        object LoadingCell : Cell()
        data class LoadedCell(val view: JokeView) : Cell()
        data class ErrorCell(val explanation: Explanation) : Cell()
    }
}
