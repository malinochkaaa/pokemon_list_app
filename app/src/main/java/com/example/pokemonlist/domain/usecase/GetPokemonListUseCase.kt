package com.example.pokemonlist.domain.usecase

import com.example.pokemonlist.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(page: Int, forceRefresh: Boolean = false) = repository.getPokemonList(page, forceRefresh)
}



