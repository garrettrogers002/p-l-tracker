package com.garrettrogers.pltracker.di;

import com.garrettrogers.pltracker.data.local.AppDatabase;
import com.garrettrogers.pltracker.data.local.PortfolioDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "KotlinInternalInJava"
})
public final class DatabaseModule_ProvidePortfolioDaoFactory implements Factory<PortfolioDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvidePortfolioDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PortfolioDao get() {
    return providePortfolioDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePortfolioDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePortfolioDaoFactory(databaseProvider);
  }

  public static PortfolioDao providePortfolioDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePortfolioDao(database));
  }
}
