package com.garrettrogers.pltracker.di;

import com.garrettrogers.pltracker.data.local.AppDatabase;
import com.garrettrogers.pltracker.data.local.TradeDao;
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
public final class DatabaseModule_ProvideTradeDaoFactory implements Factory<TradeDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvideTradeDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public TradeDao get() {
    return provideTradeDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideTradeDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvideTradeDaoFactory(databaseProvider);
  }

  public static TradeDao provideTradeDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTradeDao(database));
  }
}
