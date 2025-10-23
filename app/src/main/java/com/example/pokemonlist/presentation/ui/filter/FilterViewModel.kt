package com.example.pokemonlist.presentation.ui.filter

import androidx.lifecycle.ViewModel
import com.example.pokemonlist.domain.model.PokemonFilters
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {

    private val selectedTypes = mutableSetOf<String>()
    private var selectedGeneration: Int? = null
    private var heightRange = Pair(0, 300)
    private var weightRange = Pair(0, 1000)

    private val _filterResult = MutableStateFlow<PokemonFilters?>(null)
    val filterResult: Flow<PokemonFilters> = _filterResult.filterNotNull()

    fun toggleType(type: String, isSelected: Boolean) {
        if (isSelected) {
            selectedTypes.add(type)
        } else {
            selectedTypes.remove(type)
        }
    }

    fun setHeightRange(min: Int, max: Int) {
        heightRange = Pair(min, max)
    }

    fun setWeightRange(min: Int, max: Int) {
        weightRange = Pair(min, max)
    }

    fun applyFilters() {
        val filters = PokemonFilters(
            types = selectedTypes.toList(),
            generation = selectedGeneration,
            heightRange = heightRange,
            weightRange = weightRange
        )
        _filterResult.value = filters
    }

    fun resetFilters() {
        selectedTypes.clear()
        selectedGeneration = null
        heightRange = Pair(0, 300)
        weightRange = Pair(0, 1000)
    }
}
