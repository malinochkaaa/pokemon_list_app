package com.example.pokemonlist.domain.usecase

import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.model.Result
import com.example.pokemonlist.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(pokemonId: Int): Result<PokemonDetail> = repository.getPokemonDetail(pokemonId)
}
