package com.example.pokedex.domain.model

/**
 * Modèle métier représentant un Pokémon ajouté aux favoris.
 * Stocké en base de données Room.
 */
data class FavoritePokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>
) {
    val formattedNumber: String
        get() = "#${id.toString().padStart(3, '0')}"

    val displayName: String
        get() = name.replaceFirstChar { it.uppercase() }
}
