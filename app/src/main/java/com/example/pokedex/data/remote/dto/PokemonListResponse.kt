package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO pour la réponse de l'endpoint GET /pokemon?limit=N.
 * Contient la liste paginée des résultats Pokémon.
 */
data class PokemonListResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("results") val results: List<PokemonResultDto>
)

/**
 * DTO pour un résultat individuel dans la liste.
 * Contient uniquement le nom et l'URL vers les détails.
 */
data class PokemonResultDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
) {
    /**
     * Extrait l'ID du Pokémon depuis l'URL.
     * Ex: "https://pokeapi.co/api/v2/pokemon/25/" → 25
     */
    val id: Int
        get() = url.trimEnd('/').substringAfterLast('/').toInt()
}
