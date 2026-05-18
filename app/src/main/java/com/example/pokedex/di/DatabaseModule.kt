package com.example.pokedex.di

import android.content.Context
import androidx.room.Room
import com.example.pokedex.data.local.FavoritePokemonDao
import com.example.pokedex.data.local.PokedexDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module Hilt pour la base de données Room.
 * Fournit l'instance singleton de PokedexDatabase et le DAO des favoris.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): PokedexDatabase =
        Room.databaseBuilder(
            context,
            PokedexDatabase::class.java,
            "pokedex_database"
        ).build()

    @Provides
    fun provideFavoritePokemonDao(
        database: PokedexDatabase
    ): FavoritePokemonDao =
        database.favoritePokemonDao()
}
