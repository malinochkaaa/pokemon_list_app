package com.example.pokemonlist.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Double?,
    val weight: Double?,
    val types: List<String>?,
    val abilities: List<String>?,
) : Parcelable

