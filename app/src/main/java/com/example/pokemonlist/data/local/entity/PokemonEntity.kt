package com.example.pokemonlist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val height: Int? = null,
    val weight: Int? = null,
    val types: String? = null,
    val page: Int = 0
)
