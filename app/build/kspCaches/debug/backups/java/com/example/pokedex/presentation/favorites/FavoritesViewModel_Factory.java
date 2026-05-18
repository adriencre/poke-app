package com.example.pokedex.presentation.favorites;

import com.example.pokedex.domain.repository.FavoriteRepository;
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
public final class FavoritesViewModel_Factory implements Factory<FavoritesViewModel> {
  private final Provider<FavoriteRepository> favoriteRepositoryProvider;

  private FavoritesViewModel_Factory(Provider<FavoriteRepository> favoriteRepositoryProvider) {
    this.favoriteRepositoryProvider = favoriteRepositoryProvider;
  }

  @Override
  public FavoritesViewModel get() {
    return newInstance(favoriteRepositoryProvider.get());
  }

  public static FavoritesViewModel_Factory create(
      Provider<FavoriteRepository> favoriteRepositoryProvider) {
    return new FavoritesViewModel_Factory(favoriteRepositoryProvider);
  }

  public static FavoritesViewModel newInstance(FavoriteRepository favoriteRepository) {
    return new FavoritesViewModel(favoriteRepository);
  }
}
