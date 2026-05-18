package com.example.pokedex.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pokedex.presentation.navigation.PokedexNavigation
import com.example.pokedex.presentation.theme.PokedexTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principale de l'application PokéDex.
 * Annotée @AndroidEntryPoint pour l'injection Hilt.
 * Utilise Edge-to-Edge pour un affichage immersif.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme(dynamicColor = false) {
                PokedexNavigation()
            }
        }
    }
}
