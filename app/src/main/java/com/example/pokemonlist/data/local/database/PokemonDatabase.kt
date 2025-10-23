package com.example.pokemonlist.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pokemonlist.data.local.database.dao.PokemonDao
import com.example.pokemonlist.data.local.entity.PokemonEntity

@Database(
    version = 1,
    entities = [PokemonEntity::class]
)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun getPokemonDao(): PokemonDao
}
