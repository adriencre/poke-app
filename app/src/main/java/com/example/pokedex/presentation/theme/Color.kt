package com.example.pokedex.presentation.theme

import androidx.compose.ui.graphics.Color

// ---- Material3 Theme Colors ----

// Dark Theme
val DarkBackground = Color(0xFF1A1A2E)
val DarkSurface = Color(0xFF16213E)
val DarkSurfaceVariant = Color(0xFF0F3460)
val DarkOnBackground = Color(0xFFE8E8E8)
val DarkOnSurface = Color(0xFFE8E8E8)
val DarkPrimary = Color(0xFFE94560)
val DarkOnPrimary = Color(0xFFFFFFFF)
val DarkSecondary = Color(0xFF533483)
val DarkOnSecondary = Color(0xFFFFFFFF)

// Light Theme
val LightBackground = Color(0xFFF5F5F5)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFE8EAF6)
val LightOnBackground = Color(0xFF1A1A2E)
val LightOnSurface = Color(0xFF1A1A2E)
val LightPrimary = Color(0xFFE94560)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightSecondary = Color(0xFF533483)
val LightOnSecondary = Color(0xFFFFFFFF)

// ---- Pokémon Type Colors (Couleurs officielles) ----

object PokemonTypeColors {
    val typeColorMap: Map<String, Color> = mapOf(
        "normal" to Color(0xFFA8A77A),
        "fire" to Color(0xFFEE8130),
        "water" to Color(0xFF6390F0),
        "electric" to Color(0xFFF7D02C),
        "grass" to Color(0xFF7AC74C),
        "ice" to Color(0xFF96D9D6),
        "fighting" to Color(0xFFC22E28),
        "poison" to Color(0xFFA33EA1),
        "ground" to Color(0xFFE2BF65),
        "flying" to Color(0xFFA98FF3),
        "psychic" to Color(0xFFF95587),
        "bug" to Color(0xFFA6B91A),
        "rock" to Color(0xFFB6A136),
        "ghost" to Color(0xFF735797),
        "dragon" to Color(0xFF6F35FC),
        "dark" to Color(0xFF705746),
        "steel" to Color(0xFFB7B7CE),
        "fairy" to Color(0xFFD685AD)
    )

    /**
     * Retourne la couleur associée à un type Pokémon.
     * Fallback sur gris si le type n'est pas trouvé.
     */
    fun getColor(type: String): Color =
        typeColorMap[type.lowercase()] ?: Color(0xFF68A090)

    /**
     * Retourne une couleur de texte contrastée pour un fond de type donné.
     */
    fun getOnTypeColor(type: String): Color = Color.White
}
