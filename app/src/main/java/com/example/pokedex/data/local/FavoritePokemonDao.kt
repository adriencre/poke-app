package com.example.pokedex.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO Room pour les opérations CRUD sur les Pokémon favoris.
 * Expose des Flow pour une observation réactive automatique.
 */
@Dao
interface FavoritePokemonDao {

    /**
     * Observe la liste complète des favoris.
     * Émet automatiquement à chaque modification de la table.
     * @return Flow réactif de la liste des favoris
     */
    @Query("SELECT * FROM favorite_pokemon ORDER BY id ASC")
    fun getAllFavorites(): Flow<List<FavoritePokemonEntity>>

    /**
     * Insère ou remplace un favori.
     * @param pokemon L'entity à insérer
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(pokemon: FavoritePokemonEntity)

    /**
     * Supprime un favori par son ID.
     * @param pokemonId Identifiant du Pokémon à retirer
     */
    @Query("DELETE FROM favorite_pokemon WHERE id = :pokemonId")
    suspend fun deleteFavorite(pokemonId: Int)

    /**
     * Observe si un Pokémon est en favori.
     * @param pokemonId Identifiant du Pokémon
     * @return Flow émettant true/false réactivement
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pokemon WHERE id = :pokemonId)")
    fun isFavorite(pokemonId: Int): Flow<Boolean>
}
