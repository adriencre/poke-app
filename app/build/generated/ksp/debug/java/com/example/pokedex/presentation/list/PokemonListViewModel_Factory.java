package com.example.pokedex.presentation.list;

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
public final class PokemonListViewModel_Factory implements Factory<PokemonListViewModel> {
  private final Provider<PokemonRepository> pokemonRepositoryProvider;

  private PokemonListViewModel_Factory(Provider<PokemonRepository> pokemonRepositoryProvider) {
    this.pokemonRepositoryProvider = pokemonRepositoryProvider;
  }

  @Override
  public PokemonListViewModel get() {
    return newInstance(pokemonRepositoryProvider.get());
  }

  public static PokemonListViewModel_Factory create(
      Provider<PokemonRepository> pokemonRepositoryProvider) {
    return new PokemonListViewModel_Factory(pokemonRepositoryProvider);
  }

  public static PokemonListViewModel newInstance(PokemonRepository pokemonRepository) {
    return new PokemonListViewModel(pokemonRepository);
  }
}
