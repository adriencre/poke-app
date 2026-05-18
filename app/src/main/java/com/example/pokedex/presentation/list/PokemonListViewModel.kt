package com.example.pokedex.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.Pokemon
import com.example.pokedex.domain.repository.PokemonRepository
import com.example.pokedex.domain.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel pour l'écran de liste des Pokémon.
 * Charge les 100 premiers Pokémon au démarrage et gère le filtrage local
 * par nom (recherche) et par type (chips).
 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    /** État du chargement de la liste complète */
    private val _pokemonListState = MutableStateFlow<UiState<List<Pokemon>>>(UiState.Loading)
    val pokemonListState: StateFlow<UiState<List<Pokemon>>> = _pokemonListState.asStateFlow()

    /** Texte de recherche saisi par l'utilisateur */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /** Type actuellement sélectionné pour le filtrage (null = tous) */
    private val _selectedType = MutableStateFlow<String?>(null)
    val selectedType: StateFlow<String?> = _selectedType.asStateFlow()

    /**
     * Liste filtrée combinant la liste complète, la recherche et le filtre type.
     * Le filtrage est 100% local — aucun appel réseau supplémentaire.
     */
    val filteredPokemonList: StateFlow<UiState<List<Pokemon>>> = combine(
        _pokemonListState,
        _searchQuery,
        _selectedType
    ) { state, query, type ->
        when (state) {
            is UiState.Loading -> UiState.Loading
            is UiState.Error -> state
            is UiState.Success -> {
                val filtered = state.data.filter { pokemon ->
                    val matchesQuery = query.isBlank() ||
                            pokemon.name.contains(query, ignoreCase = true) ||
                            pokemon.formattedNumber.contains(query)
                    val matchesType = type == null ||
                            pokemon.types.any { it.equals(type, ignoreCase = true) }
                    matchesQuery && matchesType
                }
                UiState.Success(filtered)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    /**
     * Liste de tous les types disponibles, extraite de la liste chargée.
     * Utilisée pour alimenter les FilterChips.
     */
    val availableTypes: StateFlow<List<String>> = _pokemonListState
        .combine(MutableStateFlow(Unit)) { state, _ ->
            when (state) {
                is UiState.Success -> state.data
                    .flatMap { it.types }
                    .distinct()
                    .sorted()
                else -> emptyList()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadPokemonList()
    }

    /**
     * Charge les 100 premiers Pokémon depuis l'API.
     */
    fun loadPokemonList() {
        viewModelScope.launch {
            _pokemonListState.value = UiState.Loading
            try {
                val pokemonList = pokemonRepository.getPokemonList(limit = 100)
                _pokemonListState.value = UiState.Success(pokemonList.sortedBy { it.id })
            } catch (e: Exception) {
                _pokemonListState.value = UiState.Error(
                    e.localizedMessage ?: "Erreur lors du chargement des Pokémon"
                )
            }
        }
    }

    /**
     * Met à jour le texte de recherche.
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    /**
     * Met à jour le type sélectionné pour le filtrage.
     */
    fun onTypeSelected(type: String?) {
        _selectedType.value = type
    }
}
