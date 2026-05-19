package com.example.pokedex.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO pour l'endpoint /pokemon-species/{id}
 */
data class PokemonSpeciesDto(
    @SerializedName("evolution_chain") val evolutionChain: EvolutionChainUrlDto,
    @SerializedName("flavor_text_entries") val flavorTextEntries: List<FlavorTextEntryDto>?
)

data class FlavorTextEntryDto(
    @SerializedName("flavor_text") val flavorText: String,
    @SerializedName("language") val language: LanguageDto,
    @SerializedName("version") val version: VersionDto
)

data class LanguageDto(
    @SerializedName("name") val name: String
)

data class VersionDto(
    @SerializedName("name") val name: String
)

data class EvolutionChainUrlDto(
    @SerializedName("url") val url: String
)

/**
 * DTO pour l'endpoint /evolution-chain/{id}
 */
data class EvolutionChainResponseDto(
    @SerializedName("chain") val chain: EvolutionNodeDto
)

data class EvolutionNodeDto(
    @SerializedName("species") val species: SpeciesNameUrlDto,
    @SerializedName("evolves_to") val evolvesTo: List<EvolutionNodeDto>
)

data class SpeciesNameUrlDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
