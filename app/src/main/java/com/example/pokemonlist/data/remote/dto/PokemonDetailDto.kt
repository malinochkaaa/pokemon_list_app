package com.example.pokemonlist.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonDetailDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("height")
    val height: Int,

    @SerializedName("weight")
    val weight: Int,

    @SerializedName("sprites")
    val sprites: SpriteDto,

    @SerializedName("types")
    val types: List<TypeSlotDto>,
)

data class SpriteDto(
    @SerializedName("front_default")
    val frontDefault: String,
)

data class TypeSlotDto(
    @SerializedName("type")
    val type: TypeDto
)

data class TypeDto(
    @SerializedName("name")
    val name: String,
)
