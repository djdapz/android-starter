package io.damo.androidstarter.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(val joke: String, @PrimaryKey val id: Int)