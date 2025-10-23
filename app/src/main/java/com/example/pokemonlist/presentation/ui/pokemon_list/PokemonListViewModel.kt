package com.example.pokemonlist.presentation.ui.pokemon_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.model.PokemonFilters
import com.example.pokemonlist.domain.model.Result
import com.example.pokemonlist.domain.usecase.FilterPokemonsUseCase
import com.example.pokemonlist.domain.usecase.GetPokemonListUseCase
import com.example.pokemonlist.domain.usecase.SearchPokemonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonsUseCase: GetPokemonListUseCase,
    private val searchPokemonsUseCase: SearchPokemonsUseCase,
    private val filterPokemonsUseCase: FilterPokemonsUseCase
) : ViewModel() {

    private val _pokemonList = MutableStateFlow<Result<List<PokemonDetail>>>(Result.Loading())
    val pokemonList: StateFlow<Result<List<PokemonDetail>>> = _pokemonList

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var currentPage = 0
    private var isLoading = false

    private var searchJob: Job? = null

    private var currentPokemonList = mutableListOf<PokemonDetail>()

    init {
        loadPokemon()
    }

    fun loadPokemon(forceRefresh: Boolean = false) {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true
            if (forceRefresh) {
                currentPage = 0
                currentPokemonList.clear()
            }
            if (currentPage == 0) {
                _pokemonList.value = Result.Loading()
            }

            when (val result = getPokemonsUseCase(page = currentPage, forceRefresh = forceRefresh)) {
                is Result.Success -> {
                    currentPokemonList.addAll(result.data)
                    _pokemonList.value = Result.Success(currentPokemonList.toList())
                    currentPage++
                    isLoading = false
                    _isRefreshing.value = false
                }
                is Result.Error -> {
                    _pokemonList.value = result
                    isLoading = false
                    _isRefreshing.value = false
                }
                is Result.Loading -> {
                    if (currentPage == 0) {
                        _pokemonList.value = Result.Loading()
                    }
                }
            }
        }
    }

    fun applyFilters(filters: PokemonFilters) {
        viewModelScope.launch {
            _pokemonList.value = Result.Loading()
            val result = filterPokemonsUseCase(filters)
            _pokemonList.value = result
        }
    }

    fun searchPokemon(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _pokemonList.value = Result.Success(currentPokemonList.toList())
            return
        }
        searchJob = viewModelScope.launch {
            delay(300L)
            _pokemonList.value = Result.Loading()
            val result = searchPokemonsUseCase(query)
            _pokemonList.value = Result.Success(result, true)
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        loadPokemon(forceRefresh = true)
    }
}
