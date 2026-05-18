package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO pour la réponse complète de l'endpoint GET /pokemon/{id}.
 * Mappé sur la structure JSON de la PokéAPI v2.
 */
data class PokemonDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int,
    @SerializedName("types") val types: List<TypeSlotDto>,
    @SerializedName("stats") val stats: List<StatSlotDto>,
    @SerializedName("sprites") val sprites: SpritesDto
)

/**
 * DTO pour un slot de type (position + type).
 */
data class TypeSlotDto(
    @SerializedName("slot") val slot: Int,
    @SerializedName("type") val type: TypeDto
)

/**
 * DTO pour un type Pokémon.
 */
data class TypeDto(
    @SerializedName("name") val name: String
)

/**
 * DTO pour un slot de statistique (valeur + stat).
 */
data class StatSlotDto(
    @SerializedName("base_stat") val baseStat: Int,
    @SerializedName("effort") val effort: Int,
    @SerializedName("stat") val stat: StatNameDto
)

/**
 * DTO pour le nom d'une statistique.
 */
data class StatNameDto(
    @SerializedName("name") val name: String
)

/**
 * DTO pour les sprites du Pokémon.
 * Inclut le sprite par défaut et l'artwork officiel HD.
 */
data class SpritesDto(
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("other") val other: OtherSpritesDto?
)

/**
 * DTO pour les sprites alternatifs.
 */
data class OtherSpritesDto(
    @SerializedName("official-artwork") val officialArtwork: OfficialArtworkDto?
)

/**
 * DTO pour l'artwork officiel (image HD).
 */
data class OfficialArtworkDto(
    @SerializedName("front_default") val frontDefault: String?
)
