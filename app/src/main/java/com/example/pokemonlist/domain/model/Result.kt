package com.example.pokemonlist.domain.model

sealed class Result<T> {
    class Loading<T> : Result<T>()
    data class Success<T>(val data: T, val isFilterResult: Boolean = false) : Result<T>()
    data class Error<T>(val message: String) : Result<T>()
}
