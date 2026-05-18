package com.example.pokedex.domain.repository

import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonDetail

/**
 * Interface du Repository pour les opérations liées aux données Pokémon.
 * Implémentée dans la couche Data par PokemonRepositoryImpl.
 */
interface PokemonRepository {

    /**
     * Récupère la liste des Pokémon avec leurs détails (types, sprites).
     * @param limit Nombre de Pokémon à charger (défaut: 100)
     * @return Liste de Pokémon avec les informations de base
     */
    suspend fun getPokemonList(limit: Int = 100): List<Pokemon>

    /**
     * Récupère les détails complets d'un Pokémon par son ID.
     * @param id Identifiant du Pokémon
     * @return Détails complets incluant stats, taille, poids
     */
    suspend fun getPokemonDetail(id: Int): PokemonDetail
}
