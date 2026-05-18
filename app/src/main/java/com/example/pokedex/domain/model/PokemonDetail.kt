package com.example.pokedex.domain.model

/**
 * Modèle métier représentant les détails complets d'un Pokémon.
 * Utilisé sur l'écran de détail avec image HD, stats, taille et poids.
 */
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val height: Int,     // en décimètres (API)
    val weight: Int,     // en hectogrammes (API)
    val stats: List<PokemonStat>
) {
    val formattedNumber: String
        get() = "#${id.toString().padStart(3, '0')}"

    val displayName: String
        get() = name.replaceFirstChar { it.uppercase() }

    /**
     * Taille convertie en mètres (ex: 4 → 0.4 m).
     */
    val heightInMeters: String
        get() = "${height / 10.0} m"

    /**
     * Poids converti en kilogrammes (ex: 60 → 6.0 kg).
     */
    val weightInKg: String
        get() = "${weight / 10.0} kg"
}

/**
 * Représente une statistique de base d'un Pokémon.
 * @param name Nom de la stat (hp, attack, defense, special-attack, special-defense, speed)
 * @param baseStat Valeur de base de la statistique
 */
data class PokemonStat(
    val name: String,
    val baseStat: Int
) {
    /**
     * Nom affiché de la statistique, abrégé pour l'UI.
     */
    val displayName: String
        get() = when (name) {
            "hp" -> "PV"
            "attack" -> "ATQ"
            "defense" -> "DEF"
            "special-attack" -> "ATQ Spé"
            "special-defense" -> "DEF Spé"
            "speed" -> "VIT"
            else -> name.uppercase()
        }
}
