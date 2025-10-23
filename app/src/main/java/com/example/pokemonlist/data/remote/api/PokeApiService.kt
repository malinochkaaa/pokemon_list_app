package com.example.pokemonlist.data.remote.api

import com.example.pokemonlist.data.remote.dto.PokemonDetailDto
import com.example.pokemonlist.data.remote.dto.PokemonListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): PokemonListDto

    @GET("pokemon/{id}")
    suspend fun getPokemonDetailById(@Path("id") id: Int): PokemonDetailDto
}
