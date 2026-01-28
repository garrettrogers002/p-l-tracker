package com.garrettrogers.pltracker.ui.screens.analysis;

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
public final class AnalysisViewModel_Factory implements Factory<AnalysisViewModel> {
  private final Provider<TradeRepository> repositoryProvider;

  public AnalysisViewModel_Factory(Provider<TradeRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AnalysisViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static AnalysisViewModel_Factory create(Provider<TradeRepository> repositoryProvider) {
    return new AnalysisViewModel_Factory(repositoryProvider);
  }

  public static AnalysisViewModel newInstance(TradeRepository repository) {
    return new AnalysisViewModel(repository);
  }
}
