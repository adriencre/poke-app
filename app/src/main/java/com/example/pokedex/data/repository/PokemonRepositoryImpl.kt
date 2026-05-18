package com.example.pokedex.data.repository

import com.example.pokedex.data.remote.PokeApiService
import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.model.PokemonStat
import com.example.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implémentation du PokemonRepository.
 * Gère les appels réseau vers la PokéAPI et le mapping DTO → Domain.
 */
@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val apiService: PokeApiService
) : PokemonRepository {

    /**
     * Récupère la liste des Pokémon avec leurs détails.
     * 1. Appelle GET /pokemon?limit=N pour obtenir les noms et URLs
     * 2. Charge les détails de chaque Pokémon en parallèle via coroutineScope + async
     * 3. Filtre les échecs silencieusement (un Pokémon qui échoue n'empêche pas les autres)
     */
    override suspend fun getPokemonList(limit: Int): List<Pokemon> =
        withContext(Dispatchers.IO) {
            val listResponse = apiService.getPokemonList(limit = limit)

            coroutineScope {
                listResponse.results.map { result ->
                    async {
                        try {
                            val detail = apiService.getPokemonDetail(result.id)
                            detail.toPokemon()
                        } catch (e: Exception) {
                            null // Ignore les Pokémon qui échouent
                        }
                    }
                }.awaitAll().filterNotNull()
            }
        }

    /**
     * Récupère les détails complets d'un Pokémon par son ID.
     */
    override suspend fun getPokemonDetail(id: Int): PokemonDetail =
        withContext(Dispatchers.IO) {
            val dto = apiService.getPokemonDetail(id)
            dto.toPokemonDetail()
        }

    // ---- Mappers DTO → Domain ----

    /**
     * Mappe un PokemonDetailDto vers le modèle Pokemon (liste).
     */
    private fun PokemonDetailDto.toPokemon(): Pokemon = Pokemon(
        id = id,
        name = name,
        imageUrl = sprites.frontDefault
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
        types = types.sortedBy { it.slot }.map { it.type.name }
    )

    /**
     * Mappe un PokemonDetailDto vers le modèle PokemonDetail (détail).
     */
    private fun PokemonDetailDto.toPokemonDetail(): PokemonDetail = PokemonDetail(
        id = id,
        name = name,
        imageUrl = sprites.other?.officialArtwork?.frontDefault
            ?: sprites.frontDefault
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png",
        types = types.sortedBy { it.slot }.map { it.type.name },
        height = height,
        weight = weight,
        stats = stats.map { statSlot ->
            PokemonStat(
                name = statSlot.stat.name,
                baseStat = statSlot.baseStat
            )
        }
    )
}
