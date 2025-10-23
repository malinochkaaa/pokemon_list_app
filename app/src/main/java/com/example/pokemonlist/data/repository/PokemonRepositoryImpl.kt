package com.example.pokemonlist.data.repository

import com.example.pokemonlist.data.local.database.dao.PokemonDao
import com.example.pokemonlist.data.remote.api.PokeApiService
import com.example.pokemonlist.data.remote.mapper.PokemonMapper
import com.example.pokemonlist.domain.model.PokemonDetail
import com.example.pokemonlist.domain.model.PokemonFilters
import com.example.pokemonlist.domain.model.Result
import com.example.pokemonlist.domain.repository.PokemonRepository
import com.example.pokemonlist.presentation.util.extractPokemonId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val api: PokeApiService,
    private val dao: PokemonDao,
    private val mapper: PokemonMapper,
    private val gson: Gson
) : PokemonRepository {

    override suspend fun getPokemonList(page: Int, forceRefresh: Boolean): Result<List<PokemonDetail>> {
        val limit = 20
        val offset = page * limit

        if (page == 0 && forceRefresh) {
            dao.clearPokemonList()
        }

        val cachedPokemon = dao.getLimitedPokemonList(limit, offset)
        if (cachedPokemon.isNotEmpty() && !forceRefresh) {
            return Result.Success(mapper.mapEntitiesToDomain(cachedPokemon))
        }

        try {
            val listResponse = api.getPokemonList(offset = offset, limit = limit)
            val pokemonEntities = listResponse.pokemonListResult.mapNotNull { result ->
                result.detailsUrl.extractPokemonId()?.let { id ->
                    val detailDto = api.getPokemonDetailById(id)
                    mapper.mapDetailDtoToEntity(detailDto)
                }
            }

            dao.insertFullPokemonList(pokemonEntities)
            return Result.Success(mapper.mapEntitiesToDomain(pokemonEntities))

        } catch (e: IOException) {
            val recheckedCache = dao.getLimitedPokemonList(limit, offset)
            return if (recheckedCache.isNotEmpty()) {
                Result.Success(mapper.mapEntitiesToDomain(recheckedCache))
            } else {
                Result.Error(e.message ?: "Failed to load Pokemon.")
            }
        }
    }

    override suspend fun searchPokemonFromCache(query: String): List<PokemonDetail> {
        if (query.isBlank()) {
            return mapper.mapEntitiesToDomain(dao.getFullPokemonList())
        }
        val searchQuery = "%${query.trim()}%"
        val searchResult = dao.searchPokemon(searchQuery)
        return mapper.mapEntitiesToDomain(searchResult)
    }

    override suspend fun getPokemonDetail(pokemonId: Int): Result<PokemonDetail> {
        val cachedEntity = dao.getPokemonById(pokemonId)
        if (cachedEntity?.height != null) {
            return Result.Success(mapper.mapEntityToDetail(cachedEntity))
        }

        try {
            val detailDto = api.getPokemonDetailById(pokemonId)
            val entityToSave = mapper.mapDetailDtoToEntity(detailDto)
            dao.insertOrUpdatePokemon(entityToSave)
            val freshDetail = mapper.mapEntityToDetail(entityToSave)
            return Result.Success(freshDetail)
        } catch (e: IOException) {
            return if (cachedEntity != null) {
                Result.Success(mapper.mapEntityToDetail(cachedEntity))
            } else {
                Result.Error(e.message ?: "Failed to load details.")
            }
        }
    }

    override suspend fun filterPokemonInCache(filters: PokemonFilters): List<PokemonDetail> {
        val allCachedPokemon = dao.getFullPokemonList()

        val filteredList = allCachedPokemon.asSequence()
            .filter { pokemon ->
                val typeMatch = if (filters.types.isEmpty()) {
                    true
                } else {
                    val pokemonTypes: List<String> = gson.fromJson(
                        pokemon.types,
                        object : TypeToken<List<String>>() {}.type
                    )
                    pokemonTypes.any { it in filters.types.map { filter -> filter.lowercase() } }
                }
                typeMatch
            }
            .filter { pokemon ->
                val height = pokemon.height ?: 0
                height in filters.heightRange.first..filters.heightRange.second
            }
            .filter { pokemon ->
                val weight = pokemon.weight ?: 0
                weight in filters.weightRange.first..filters.weightRange.second
            }
            .toList()

        return mapper.mapEntitiesToDomain(filteredList)
    }
}
