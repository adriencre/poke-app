package com.example.pokedex.domain.util

/**
 * Classe scellée représentant les différents états de l'UI.
 * Utilisée pour wrapper les résultats des appels réseau et
 * gérer les états Loading, Success et Error de manière type-safe.
 */
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
