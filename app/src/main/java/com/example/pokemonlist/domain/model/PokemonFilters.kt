package com.example.pokemonlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonFilters(
    val types: List<String> = emptyList(),
    val generation: Int? = null,
    val heightRange: Pair<Int, Int> = Pair(0, 300),
    val weightRange: Pair<Int, Int> = Pair(0, 1000)
) : Parcelable
