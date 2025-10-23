package com.example.pokemonlist.domain.usecase

import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.repository.PokemonRepository
import javax.inject.Inject

class SearchPokemonsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(query: String): List<PokemonDetail> = if (query.isBlank()) {
        emptyList()
    } else {
        repository.searchPokemonFromCache(query.lowercase())
    }
}
