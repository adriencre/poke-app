package com.example.pokedex.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.model.FavoritePokemon
import com.example.pokedex.domain.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel pour l'écran des favoris.
 * Observe le Flow Room des favoris et l'expose en StateFlow
 * pour une mise à jour réactive automatique de l'UI.
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoriteRepository: FavoriteRepository
) : ViewModel() {

    /**
     * Liste des Pokémon favoris, mise à jour réactivement
     * à chaque modification de la table Room.
     */
    val favorites: StateFlow<List<FavoritePokemon>> = favoriteRepository
        .getAllFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
