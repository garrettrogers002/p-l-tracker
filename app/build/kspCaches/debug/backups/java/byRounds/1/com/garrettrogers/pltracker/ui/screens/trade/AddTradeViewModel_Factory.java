package com.garrettrogers.pltracker.ui.screens.trade;

import com.garrettrogers.pltracker.data.repository.TradeRepository;
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
public final class AddTradeViewModel_Factory implements Factory<AddTradeViewModel> {
  private final Provider<TradeRepository> repositoryProvider;

  public AddTradeViewModel_Factory(Provider<TradeRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddTradeViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddTradeViewModel_Factory create(Provider<TradeRepository> repositoryProvider) {
    return new AddTradeViewModel_Factory(repositoryProvider);
  }

  public static AddTradeViewModel newInstance(TradeRepository repository) {
    return new AddTradeViewModel(repository);
  }
}
