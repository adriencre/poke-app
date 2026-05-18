package com.example.pokedex.domain.model

/**
 * Modèle métier représentant un Pokémon dans la liste principale.
 * Contient les informations essentielles pour l'affichage dans la grille.
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>
) {
    /**
     * Retourne le numéro formaté du Pokémon (ex: #001, #025, #150).
     */
    val formattedNumber: String
        get() = "#${id.toString().padStart(3, '0')}"

    /**
     * Retourne le nom avec la première lettre en majuscule.
     */
    val displayName: String
        get() = name.replaceFirstChar { it.uppercase() }
}
