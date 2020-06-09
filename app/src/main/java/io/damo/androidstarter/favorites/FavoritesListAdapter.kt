package io.damo.androidstarter.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import io.damo.androidstarter.R
import io.damo.androidstarter.appComponent

class FavoritesListAdapter(context: Context) :
    BaseAdapter() {

    private val layoutInflater = LayoutInflater.from(context)
    private val favoritesRepo = context.appComponent.favoritesRepo

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: layoutInflater.inflate(
            R.layout.cell_favorite,
            parent,
            false
        )

        val textView: TextView = view.findViewById(R.id.savedJoke) as TextView
        val button: ImageButton = view.findViewById(R.id.removeFromFavorites) as ImageButton

        val favorite: String = getItem(position) as String

        textView.text = favorite
        button.setOnClickListener {
            favoritesRepo.remove(favorite)
            this.notifyDataSetChanged()
        }

        return view
    }

    override fun getItem(position: Int): Any = favoritesRepo.getFavorites()[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = favoritesRepo.getFavorites().size

}
