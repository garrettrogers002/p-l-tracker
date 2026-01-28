package com.garrettrogers.pltracker.ui.screens.settings;

import android.app.Application;
import com.garrettrogers.pltracker.data.local.UserPreferences;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<UserPreferences> userPreferencesProvider;

  private final Provider<TradeRepository> repositoryProvider;

  private final Provider<Application> applicationProvider;

  public SettingsViewModel_Factory(Provider<UserPreferences> userPreferencesProvider,
      Provider<TradeRepository> repositoryProvider, Provider<Application> applicationProvider) {
    this.userPreferencesProvider = userPreferencesProvider;
    this.repositoryProvider = repositoryProvider;
    this.applicationProvider = applicationProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(userPreferencesProvider.get(), repositoryProvider.get(), applicationProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<UserPreferences> userPreferencesProvider,
      Provider<TradeRepository> repositoryProvider, Provider<Application> applicationProvider) {
    return new SettingsViewModel_Factory(userPreferencesProvider, repositoryProvider, applicationProvider);
  }

  public static SettingsViewModel newInstance(UserPreferences userPreferences,
      TradeRepository repository, Application application) {
    return new SettingsViewModel(userPreferences, repository, application);
  }
}
