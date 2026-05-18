package com.example.pokedex.presentation.detail;

import androidx.lifecycle.SavedStateHandle;
import com.example.pokedex.domain.repository.FavoriteRepository;
import com.example.pokedex.domain.repository.PokemonRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class PokemonDetailViewModel_Factory implements Factory<PokemonDetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<PokemonRepository> pokemonRepositoryProvider;

  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private PokemonDetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<PokemonRepository> pokemonRepositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.pokemonRepositoryProvider = pokemonRepositoryProvider;
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
  }

  @Override
  public PokemonDetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), pokemonRepositoryProvider.get(), favoriteRepositoryProvider.get());
  }

  public static PokemonDetailViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<PokemonRepository> pokemonRepositoryProvider,
      Provider<FavoriteRepository> favoriteRepositoryProvider) {
    return new PokemonDetailViewModel_Factory(savedStateHandleProvider, pokemonRepositoryProvider, favoriteRepositoryProvider);
  }

  public static PokemonDetailViewModel newInstance(SavedStateHandle savedStateHandle,
      PokemonRepository pokemonRepository, FavoriteRepository favoriteRepository) {
    return new PokemonDetailViewModel(savedStateHandle, pokemonRepository, favoriteRepository);
  }
}
