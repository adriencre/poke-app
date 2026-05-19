package com.example.pokedex.domain.model

import com.example.pokedex.domain.util.TypeEffectivenessCalculator

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
    val stats: List<PokemonStat>,
    val cryUrl: String?, // URL du cri (son)
    val evolutions: List<Evolution> = emptyList(), // Chaîne d'évolution
    val shinyImageUrl: String? = null,
    val abilities: List<PokemonAbility> = emptyList(),
    val moves: List<PokemonMove> = emptyList(),
    val flavorTexts: List<PokemonFlavorText> = emptyList()
) {
    val weaknessesAndResistances: Map<String, Float>
        get() = TypeEffectivenessCalculator.getWeaknessesAndResistances(types)
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

data class Evolution(
    val id: Int,
    val name: String,
    val imageUrl: String
)

data class PokemonAbility(
    val name: String,
    val isHidden: Boolean
) {
    val displayName: String
        get() = name.replace("-", " ").replaceFirstChar { it.uppercase() }
}

data class PokemonMove(
    val name: String,
    val levelLearnedAt: Int,
    val type: String,
    val power: Int?,
    val accuracy: Int?
) {
    val displayName: String
        get() = name.replace("-", " ").replaceFirstChar { it.uppercase() }
}

data class PokemonFlavorText(
    val text: String,
    val version: String
) {
    val displayVersion: String
        get() = version.replace("-", " ").replaceFirstChar { it.uppercase() }
}
