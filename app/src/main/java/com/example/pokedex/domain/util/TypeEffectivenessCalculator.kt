package com.example.pokedex.domain.util

object TypeEffectivenessCalculator {
    // Multiplicateur de dégâts : Type d'attaque (clé primaire) -> Map<Type défenseur, multiplicateur>
    private val typeChart = mapOf(
        "normal" to mapOf("rock" to 0.5f, "ghost" to 0f, "steel" to 0.5f),
        "fire" to mapOf("fire" to 0.5f, "water" to 0.5f, "grass" to 2f, "ice" to 2f, "bug" to 2f, "rock" to 0.5f, "dragon" to 0.5f, "steel" to 2f),
        "water" to mapOf("fire" to 2f, "water" to 0.5f, "grass" to 0.5f, "ground" to 2f, "rock" to 2f, "dragon" to 0.5f),
        "electric" to mapOf("water" to 2f, "electric" to 0.5f, "grass" to 0.5f, "ground" to 0f, "flying" to 2f, "dragon" to 0.5f),
        "grass" to mapOf("fire" to 0.5f, "water" to 2f, "grass" to 0.5f, "poison" to 0.5f, "ground" to 2f, "flying" to 0.5f, "bug" to 0.5f, "rock" to 2f, "dragon" to 0.5f, "steel" to 0.5f),
        "ice" to mapOf("fire" to 0.5f, "water" to 0.5f, "grass" to 2f, "ice" to 0.5f, "ground" to 2f, "flying" to 2f, "dragon" to 2f, "steel" to 0.5f),
        "fighting" to mapOf("normal" to 2f, "ice" to 2f, "poison" to 0.5f, "flying" to 0.5f, "psychic" to 0.5f, "bug" to 0.5f, "rock" to 2f, "ghost" to 0f, "dark" to 2f, "steel" to 2f, "fairy" to 0.5f),
        "poison" to mapOf("grass" to 2f, "poison" to 0.5f, "ground" to 0.5f, "rock" to 0.5f, "ghost" to 0.5f, "steel" to 0f, "fairy" to 2f),
        "ground" to mapOf("fire" to 2f, "electric" to 2f, "grass" to 0.5f, "poison" to 2f, "flying" to 0f, "bug" to 0.5f, "rock" to 2f, "steel" to 2f),
        "flying" to mapOf("electric" to 0.5f, "grass" to 2f, "fighting" to 2f, "bug" to 2f, "rock" to 0.5f, "steel" to 0.5f),
        "psychic" to mapOf("fighting" to 2f, "poison" to 2f, "psychic" to 0.5f, "dark" to 0f, "steel" to 0.5f),
        "bug" to mapOf("fire" to 0.5f, "grass" to 2f, "fighting" to 0.5f, "poison" to 0.5f, "flying" to 0.5f, "psychic" to 2f, "ghost" to 0.5f, "dark" to 2f, "steel" to 0.5f, "fairy" to 0.5f),
        "rock" to mapOf("fire" to 2f, "ice" to 2f, "fighting" to 0.5f, "ground" to 0.5f, "flying" to 2f, "bug" to 2f, "steel" to 0.5f),
        "ghost" to mapOf("normal" to 0f, "psychic" to 2f, "ghost" to 2f, "dark" to 0.5f),
        "dragon" to mapOf("dragon" to 2f, "steel" to 0.5f, "fairy" to 0f),
        "dark" to mapOf("fighting" to 0.5f, "psychic" to 2f, "ghost" to 2f, "dark" to 0.5f, "fairy" to 0.5f),
        "steel" to mapOf("fire" to 0.5f, "water" to 0.5f, "electric" to 0.5f, "ice" to 2f, "rock" to 2f, "steel" to 0.5f, "fairy" to 2f),
        "fairy" to mapOf("fire" to 0.5f, "fighting" to 2f, "poison" to 0.5f, "dragon" to 2f, "dark" to 2f, "steel" to 0.5f)
    )

    fun getWeaknessesAndResistances(defenderTypes: List<String>): Map<String, Float> {
        val allTypes = listOf("normal", "fire", "water", "electric", "grass", "ice", "fighting", "poison", "ground", "flying", "psychic", "bug", "rock", "ghost", "dragon", "dark", "steel", "fairy")
        val result = mutableMapOf<String, Float>()

        for (attackType in allTypes) {
            var multiplier = 1.0f
            for (defenderType in defenderTypes) {
                // Si l'attaque n'est pas dans le chart, multiplicateur = 1
                val typeMultipliers = typeChart[attackType]
                if (typeMultipliers != null && typeMultipliers.containsKey(defenderType)) {
                    multiplier *= typeMultipliers[defenderType]!!
                }
            }
            if (multiplier != 1.0f) {
                result[attackType] = multiplier
            }
        }
        return result
    }
}
