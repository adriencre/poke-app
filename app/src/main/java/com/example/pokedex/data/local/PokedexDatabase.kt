package com.example.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Base de données Room pour l'application PokéDex.
 * Contient la table des Pokémon favoris.
 */
@Database(
    entities = [FavoritePokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PokedexDatabase : RoomDatabase() {

    /**
     * Fournit l'accès au DAO des favoris.
     */
    abstract fun favoritePokemonDao(): FavoritePokemonDao
}
