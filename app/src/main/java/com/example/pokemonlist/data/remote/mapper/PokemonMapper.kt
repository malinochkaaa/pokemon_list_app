package com.example.pokemonlist.data.remote.mapper

import com.example.pokemonlist.data.local.entity.PokemonEntity
import com.example.pokemonlist.data.remote.dto.PokemonDetailDto
import com.example.pokemonlist.domain.model.PokemonDetail
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonMapper @Inject constructor(
    private val gson: Gson
) {

    fun mapEntitiesToDomain(entities: List<PokemonEntity>): List<PokemonDetail> =
        entities.map { entity ->
            mapEntityToDomain(entity)
        }

    private fun mapEntityToDomain(entity: PokemonEntity) = PokemonDetail(
        id = entity.id,
        name = entity.name,
        imageUrl = entity.imageUrl,
        height = null,
        weight = null,
        types = createTypeList(entity),
        abilities = null,
    )

    fun mapEntityToDetail(entity: PokemonEntity) = PokemonDetail(
        id = entity.id,
        name = entity.name,
        imageUrl = entity.imageUrl,
        height = entity.height?.div(10.0) ?: 0.0,
        weight = entity.weight?.div(10.0) ?: 0.0,
        types = createTypeList(entity),
        abilities = emptyList()
    )

    fun mapDetailDtoToEntity(dto: PokemonDetailDto) = PokemonEntity(
        id = dto.id,
        name = dto.name,
        imageUrl = dto.sprites.frontDefault,
        height = dto.height,
        weight = dto.weight,
        types = gson.toJson(dto.types.map { it.type.name }),
    )

    private fun createTypeList(entity: PokemonEntity): List<String> = try {
        val typeToken = object : TypeToken<List<String>>() {}.type
        gson.fromJson(entity.types, typeToken) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
}
