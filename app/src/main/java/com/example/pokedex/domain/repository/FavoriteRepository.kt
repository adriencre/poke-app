package com.example.pokedex.domain.repository

import com.example.pokedex.domain.model.FavoritePokemon
import kotlinx.coroutines.flow.Flow

/**
 * Interface du Repository pour la gestion des Pokémon favoris.
 * Implémentée dans la couche Data par FavoriteRepositoryImpl.
 * Expose des Flow pour une mise à jour réactive automatique.
 */
interface FavoriteRepository {

    /**
     * Observe la liste complète des Pokémon favoris.
     * @return Flow émettant la liste mise à jour à chaque changement
     */
    fun getAllFavorites(): Flow<List<FavoritePokemon>>

    /**
     * Ajoute un Pokémon aux favoris.
     * @param pokemon Le Pokémon à ajouter
     */
    suspend fun addFavorite(pokemon: FavoritePokemon)

    /**
     * Retire un Pokémon des favoris par son ID.
     * @param pokemonId Identifiant du Pokémon à retirer
     */
    suspend fun removeFavorite(pokemonId: Int)

    /**
     * Observe si un Pokémon est dans les favoris.
     * @param pokemonId Identifiant du Pokémon
     * @return Flow émettant true/false réactivement
     */
    fun isFavorite(pokemonId: Int): Flow<Boolean>
}
