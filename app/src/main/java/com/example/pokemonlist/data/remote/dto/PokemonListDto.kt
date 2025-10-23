package com.example.pokemonlist.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonListDto(
    @SerializedName("results")
    val pokemonListResult: List<PokemonResultDto>
)

data class PokemonResultDto(
    @SerializedName("url")
    val detailsUrl: String
)
