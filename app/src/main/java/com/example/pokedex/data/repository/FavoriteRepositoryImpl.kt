package com.example.pokedex.data.repository

import com.example.pokedex.data.local.FavoritePokemonDao
import com.example.pokedex.data.local.FavoritePokemonEntity
import com.example.pokedex.domain.model.FavoritePokemon
import com.example.pokedex.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implémentation du FavoriteRepository.
 * Délègue toutes les opérations au FavoritePokemonDao Room.
 * Les Flow garantissent une mise à jour réactive automatique de l'UI.
 */
@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoritePokemonDao: FavoritePokemonDao
) : FavoriteRepository {

    /**
     * Observe tous les favoris et mappe Entity → Domain.
     */
    override fun getAllFavorites(): Flow<List<FavoritePokemon>> =
        favoritePokemonDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }

    /**
     * Ajoute un favori en mappant Domain → Entity.
     */
    override suspend fun addFavorite(pokemon: FavoritePokemon) {
        favoritePokemonDao.insertFavorite(pokemon.toEntity())
    }

    /**
     * Supprime un favori par son ID.
     */
    override suspend fun removeFavorite(pokemonId: Int) {
        favoritePokemonDao.deleteFavorite(pokemonId)
    }

    /**
     * Observe si un Pokémon est en favori.
     */
    override fun isFavorite(pokemonId: Int): Flow<Boolean> =
        favoritePokemonDao.isFavorite(pokemonId)

    // ---- Mappers ----

    private fun FavoritePokemonEntity.toDomain(): FavoritePokemon = FavoritePokemon(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = FavoritePokemonEntity.stringToTypes(types)
    )

    private fun FavoritePokemon.toEntity(): FavoritePokemonEntity = FavoritePokemonEntity(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = FavoritePokemonEntity.typesToString(types)
    )
}
