package com.example.pokedex.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Classe Application annotée @HiltAndroidApp.
 * Point d'entrée pour l'injection de dépendances Dagger Hilt.
 * Doit être déclarée dans AndroidManifest.xml.
 */
@HiltAndroidApp
class PokedexApp : Application()
