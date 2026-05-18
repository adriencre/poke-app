package com.example.pokedex.data.repository;

import com.example.pokedex.data.local.FavoritePokemonDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FavoriteRepositoryImpl_Factory implements Factory<FavoriteRepositoryImpl> {
  private final Provider<FavoritePokemonDao> favoritePokemonDaoProvider;

  private FavoriteRepositoryImpl_Factory(Provider<FavoritePokemonDao> favoritePokemonDaoProvider) {
    this.favoritePokemonDaoProvider = favoritePokemonDaoProvider;
  }

  @Override
  public FavoriteRepositoryImpl get() {
    return newInstance(favoritePokemonDaoProvider.get());
  }

  public static FavoriteRepositoryImpl_Factory create(
      Provider<FavoritePokemonDao> favoritePokemonDaoProvider) {
    return new FavoriteRepositoryImpl_Factory(favoritePokemonDaoProvider);
  }

  public static FavoriteRepositoryImpl newInstance(FavoritePokemonDao favoritePokemonDao) {
    return new FavoriteRepositoryImpl(favoritePokemonDao);
  }
}
