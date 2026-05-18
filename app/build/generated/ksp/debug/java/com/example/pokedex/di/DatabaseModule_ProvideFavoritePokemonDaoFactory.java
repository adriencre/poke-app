package com.example.pokedex.di;

import com.example.pokedex.data.local.FavoritePokemonDao;
import com.example.pokedex.data.local.PokedexDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideFavoritePokemonDaoFactory implements Factory<FavoritePokemonDao> {
  private final Provider<PokedexDatabase> databaseProvider;

  private DatabaseModule_ProvideFavoritePokemonDaoFactory(
      Provider<PokedexDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public FavoritePokemonDao get() {
    return provideFavoritePokemonDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideFavoritePokemonDaoFactory create(
      Provider<PokedexDatabase> databaseProvider) {
    return new DatabaseModule_ProvideFavoritePokemonDaoFactory(databaseProvider);
  }

  public static FavoritePokemonDao provideFavoritePokemonDao(PokedexDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideFavoritePokemonDao(database));
  }
}
