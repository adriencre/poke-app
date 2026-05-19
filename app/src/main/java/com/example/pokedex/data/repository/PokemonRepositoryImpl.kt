package com.example.pokedex.data.repository

import com.example.pokedex.data.remote.PokeApiService
import com.example.pokedex.data.remote.dto.PokemonDetailDto
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.model.PokemonStat
import com.example.pokedex.domain.model.Evolution
import com.example.pokedex.domain.model.PokemonAbility
import com.example.pokedex.domain.model.PokemonMove
import com.example.pokedex.domain.model.PokemonFlavorText
import com.example.pokedex.domain.util.MoveDatabase
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
            val detail = dto.toPokemonDetail()
            
            try {
                val species = apiService.getPokemonSpecies(id)
                val evolutionUrl = species.evolutionChain.url
                val evolutionResponse = apiService.getEvolutionChain(evolutionUrl)
                val evolutions = extractEvolutions(evolutionResponse.chain)
                
                // Extraire les descriptions en français (fr)
                val flavorTexts = species.flavorTextEntries
                    ?.filter { it.language.name == "fr" }
                    ?.map { PokemonFlavorText(text = it.flavorText.replace("\n", " ").replace("\u000c", " "), version = it.version.name) }
                    ?: emptyList()

                detail.copy(evolutions = evolutions, flavorTexts = flavorTexts)
            } catch (e: Exception) {
                e.printStackTrace()
                detail
            }
        }

    private fun extractEvolutions(node: com.example.pokedex.data.remote.dto.EvolutionNodeDto): List<Evolution> {
        val evolutions = mutableListOf<Evolution>()
        var currentNode: com.example.pokedex.data.remote.dto.EvolutionNodeDto? = node
        
        while (currentNode != null) {
            val idStr = currentNode.species.url.trimEnd('/').substringAfterLast('/')
            val id = idStr.toIntOrNull() ?: 1
            evolutions.add(
                Evolution(
                    id = id,
                    name = currentNode.species.name,
                    imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                )
            )
            // On simplifie en prenant la première branche d'évolution (par ex. Évoli a plusieurs branches, on prend la 1ère)
            currentNode = currentNode.evolvesTo.firstOrNull()
        }
        return evolutions
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
        shinyImageUrl = sprites.other?.officialArtwork?.frontShiny
            ?: sprites.frontDefault
            ?: "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/shiny/$id.png",
        types = types.sortedBy { it.slot }.map { it.type.name },
        height = height,
        weight = weight,
        stats = stats.map { statSlot ->
            PokemonStat(
                name = statSlot.stat.name,
                baseStat = statSlot.baseStat
            )
        },
        cryUrl = cries?.latest ?: cries?.legacy,
        abilities = abilities?.map { PokemonAbility(name = it.ability.name, isHidden = it.isHidden) } ?: emptyList(),
        moves = moves?.filter { moveSlot ->
            moveSlot.versionGroupDetails.any { it.moveLearnMethod.name == "level-up" }
        }?.map { moveSlot ->
            val levelDetail = moveSlot.versionGroupDetails.firstOrNull { it.moveLearnMethod.name == "level-up" }
            val level = levelDetail?.levelLearnedAt ?: 1
            val moveInfo = MoveDatabase.getMoveInfo(moveSlot.move.name)
            PokemonMove(
                name = moveSlot.move.name,
                levelLearnedAt = level,
                type = moveInfo.type,
                power = moveInfo.power,
                accuracy = moveInfo.accuracy
            )
        }?.sortedBy { it.levelLearnedAt } ?: emptyList()
    )
}
