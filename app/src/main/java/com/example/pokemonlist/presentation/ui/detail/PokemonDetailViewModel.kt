package com.example.pokemonlist.presentation.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.model.Result
import com.example.pokemonlist.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _pokemonDetail = MutableStateFlow<Result<PokemonDetail>>(Result.Loading())
    val pokemonDetail: StateFlow<Result<PokemonDetail>> = _pokemonDetail

    fun loadPokemonDetail(pokemonId: Int) {
        viewModelScope.launch {
            _pokemonDetail.value = Result.Loading()
            val result = getPokemonDetailUseCase(pokemonId)
            _pokemonDetail.value = result
        }
    }
}
