package com.garrettrogers.pltracker.data.repository;

import com.garrettrogers.pltracker.data.local.PortfolioDao;
import com.garrettrogers.pltracker.data.local.TradeDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
    "KotlinInternalInJava"
})
public final class TradeRepositoryImpl_Factory implements Factory<TradeRepositoryImpl> {
  private final Provider<TradeDao> tradeDaoProvider;

  private final Provider<PortfolioDao> portfolioDaoProvider;

  public TradeRepositoryImpl_Factory(Provider<TradeDao> tradeDaoProvider,
      Provider<PortfolioDao> portfolioDaoProvider) {
    this.tradeDaoProvider = tradeDaoProvider;
    this.portfolioDaoProvider = portfolioDaoProvider;
  }

  @Override
  public TradeRepositoryImpl get() {
    return newInstance(tradeDaoProvider.get(), portfolioDaoProvider.get());
  }

  public static TradeRepositoryImpl_Factory create(Provider<TradeDao> tradeDaoProvider,
      Provider<PortfolioDao> portfolioDaoProvider) {
    return new TradeRepositoryImpl_Factory(tradeDaoProvider, portfolioDaoProvider);
  }

  public static TradeRepositoryImpl newInstance(TradeDao tradeDao, PortfolioDao portfolioDao) {
    return new TradeRepositoryImpl(tradeDao, portfolioDao);
  }
}
