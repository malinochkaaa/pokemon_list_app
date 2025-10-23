package com.example.pokemonlist.presentation.util

import com.example.pokemonlist.R

fun getTypeColor(type: String): Int {
    return when (type.lowercase()) {
        "fire" -> R.color.type_fire
        "water" -> R.color.type_water
        "grass" -> R.color.type_grass
        "electric" -> R.color.type_electric
        "psychic" -> R.color.type_psychic
        else -> R.color.type_normal
    }
}
