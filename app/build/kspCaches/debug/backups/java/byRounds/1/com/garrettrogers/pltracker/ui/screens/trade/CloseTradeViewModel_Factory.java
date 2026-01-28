package com.garrettrogers.pltracker.ui.screens.trade;

import androidx.lifecycle.SavedStateHandle;
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
public final class CloseTradeViewModel_Factory implements Factory<CloseTradeViewModel> {
  private final Provider<TradeRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public CloseTradeViewModel_Factory(Provider<TradeRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public CloseTradeViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateHandleProvider.get());
  }

  public static CloseTradeViewModel_Factory create(Provider<TradeRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new CloseTradeViewModel_Factory(repositoryProvider, savedStateHandleProvider);
  }

  public static CloseTradeViewModel newInstance(TradeRepository repository,
      SavedStateHandle savedStateHandle) {
    return new CloseTradeViewModel(repository, savedStateHandle);
  }
}
