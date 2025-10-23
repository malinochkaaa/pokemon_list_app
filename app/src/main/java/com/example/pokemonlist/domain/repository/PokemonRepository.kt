package com.example.pokemonlist.domain.repository

import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.model.PokemonFilters
import com.example.pokemonlist.domain.model.Result

interface PokemonRepository {
    suspend fun getPokemonList(page: Int, forceRefresh: Boolean): Result<List<PokemonDetail>>
    suspend fun searchPokemonFromCache(query: String): List<PokemonDetail>
    suspend fun filterPokemonInCache(filters: PokemonFilters): List<PokemonDetail>
    suspend fun getPokemonDetail(pokemonId: Int): Result<PokemonDetail>
}
