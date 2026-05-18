package com.example.pokedex.data.remote

import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.data.remote.dto.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Service Retrofit pour la PokéAPI v2.
 * Base URL : https://pokeapi.co/api/v2/
 */
interface PokeApiService {

    /**
     * Récupère la liste paginée des Pokémon.
     * @param limit Nombre de Pokémon à récupérer
     * @param offset Décalage pour la pagination
     * @return Réponse contenant la liste des noms et URLs
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    /**
     * Récupère les détails complets d'un Pokémon par son ID.
     * @param id Identifiant du Pokémon
     * @return Détails complets incluant types, stats, sprites
     */
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetailDto
}
