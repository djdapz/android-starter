package io.damo.androidstarter.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import io.damo.androidstarter.R
import io.damo.androidstarter.support.observe

class FavoritesListAdapter(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    private val favoritesRepo: FavoritesRepo
) :
    BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)

    init {
        favoritesRepo.favoritesSubscription.observe(lifecycleOwner) {
            this.notifyDataSetChanged()
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(
            R.layout.cell_favorite,
            parent,
            false
        )

        val textView: TextView = view.findViewById(R.id.savedJoke) as TextView
        val button: ImageButton = view.findViewById(R.id.removeFromFavorites) as ImageButton

        val favorite: Favorite = getItem(position) as Favorite

        textView.text = favorite.joke
        button.setOnClickListener {
            val deletingJokeId = favoritesRepo.getFavorites()[position].id
            favoritesRepo.remove(deletingJokeId)
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return favoritesRepo.getFavorites()[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return favoritesRepo.getFavorites().size
    }

}
