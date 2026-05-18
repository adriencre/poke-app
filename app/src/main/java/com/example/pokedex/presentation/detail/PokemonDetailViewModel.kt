package com.example.pokedex.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.FavoritePokemon
import com.example.pokedex.domain.model.PokemonDetail
import com.example.pokedex.domain.repository.FavoriteRepository
import com.example.pokedex.domain.repository.PokemonRepository
import com.example.pokedex.domain.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel pour l'écran de détail d'un Pokémon.
 * Reçoit l'ID via SavedStateHandle et charge les détails + état favori.
 */
@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val pokemonRepository: PokemonRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["pokemonId"])

    /** État du chargement des détails */
    private val _detailState = MutableStateFlow<UiState<PokemonDetail>>(UiState.Loading)
    val detailState: StateFlow<UiState<PokemonDetail>> = _detailState.asStateFlow()

    /**
     * Observe réactivement si le Pokémon est en favori.
     * Collecté depuis le Flow Room → StateFlow pour Compose.
     */
    val isFavorite: StateFlow<Boolean> = favoriteRepository
        .isFavorite(pokemonId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        loadDetail()
    }

    /**
     * Charge les détails complets du Pokémon.
     */
    private fun loadDetail() {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val detail = pokemonRepository.getPokemonDetail(pokemonId)
                _detailState.value = UiState.Success(detail)
            } catch (e: Exception) {
                _detailState.value = UiState.Error(
                    e.localizedMessage ?: "Erreur lors du chargement du détail"
                )
            }
        }
    }

    /**
     * Bascule l'état favori du Pokémon.
     * Si déjà en favori → retire, sinon → ajoute.
     */
    fun toggleFavorite() {
        val currentDetail = (_detailState.value as? UiState.Success)?.data ?: return
        viewModelScope.launch {
            if (isFavorite.value) {
                favoriteRepository.removeFavorite(pokemonId)
            } else {
                favoriteRepository.addFavorite(
                    FavoritePokemon(
                        id = currentDetail.id,
                        name = currentDetail.name,
                        imageUrl = currentDetail.imageUrl,
                        types = currentDetail.types
                    )
                )
            }
        }
    }
}
