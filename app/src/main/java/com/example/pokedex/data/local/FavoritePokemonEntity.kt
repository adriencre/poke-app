package com.example.pokedex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity Room représentant un Pokémon favori stocké en base locale.
 * Les types sont sérialisés en String séparés par des virgules
 * pour éviter la complexité d'un TypeConverter complet.
 */
@Entity(tableName = "favorite_pokemon")
data class FavoritePokemonEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: String // Sérialisé : "fire,flying"
) {
    companion object {
        /**
         * Convertit une liste de types en String sérialisée.
         */
        fun typesToString(types: List<String>): String = types.joinToString(",")

        /**
         * Convertit une String sérialisée en liste de types.
         */
        fun stringToTypes(types: String): List<String> =
            if (types.isBlank()) emptyList() else types.split(",")
    }
}
