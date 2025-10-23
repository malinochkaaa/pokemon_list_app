package com.example.pokemonlist.domain.usecase

import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.model.PokemonFilters
import com.example.pokemonlist.domain.model.Result
import com.example.pokemonlist.domain.repository.PokemonRepository
import javax.inject.Inject

class FilterPokemonsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(filters: PokemonFilters): Result<List<PokemonDetail>> = try {
        Result.Success(repository.filterPokemonInCache(filters), true)
    } catch (e: Exception) {
        Result.Error(e.message ?: "Error filtering pokemon")
    }
}
