package com.example.pokedex.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CatchingPokemon
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.presentation.detail.PokemonDetailScreen
import com.example.pokedex.presentation.favorites.FavoritesScreen
import com.example.pokedex.presentation.list.PokemonListScreen

/**
 * Routes de navigation de l'application.
 */
object Routes {
    const val POKEDEX_LIST = "pokedex_list"
    const val POKEMON_DETAIL = "pokemon_detail/{pokemonId}"
    const val FAVORITES = "favorites"

    fun pokemonDetail(pokemonId: Int) = "pokemon_detail/$pokemonId"
}

/**
 * Définition des onglets de la Bottom Navigation Bar.
 */
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Pokedex : BottomNavItem(
        route = Routes.POKEDEX_LIST,
        label = "Pokédex",
        selectedIcon = Icons.Filled.CatchingPokemon,
        unselectedIcon = Icons.Outlined.CatchingPokemon
    )
    data object Favorites : BottomNavItem(
        route = Routes.FAVORITES,
        label = "Favoris",
        selectedIcon = Icons.Filled.Favorite,
        unselectedIcon = Icons.Outlined.FavoriteBorder
    )
}

/**
 * Composant principal de navigation avec Bottom Navigation Bar.
 * Gère deux onglets (Pokédex, Favoris) avec back stacks indépendants.
 */
@Composable
fun PokedexNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(BottomNavItem.Pokedex, BottomNavItem.Favorites)

    // Masquer la bottom bar sur l'écran de détail
    val showBottomBar = currentDestination?.route?.startsWith("pokemon_detail") != true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.POKEDEX_LIST
        ) {
            composable(Routes.POKEDEX_LIST) {
                PokemonListScreen(
                    onPokemonClick = { pokemonId ->
                        navController.navigate(Routes.pokemonDetail(pokemonId))
                    }
                )
            }

            composable(
                route = Routes.POKEMON_DETAIL,
                arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
            ) {
                PokemonDetailScreen(
                    onBackClick = { navController.popBackStack() },
                    onPokemonClick = { pokemonId ->
                        navController.navigate(Routes.pokemonDetail(pokemonId))
                    }
                )
            }

            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    onPokemonClick = { pokemonId ->
                        navController.navigate(Routes.pokemonDetail(pokemonId))
                    }
                )
            }
        }
    }
}
