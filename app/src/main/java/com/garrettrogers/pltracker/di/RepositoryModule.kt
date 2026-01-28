package com.garrettrogers.pltracker.di

import com.garrettrogers.pltracker.data.repository.TradeRepository
import com.garrettrogers.pltracker.data.repository.TradeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTradeRepository(
        tradeRepositoryImpl: TradeRepositoryImpl
    ): TradeRepository
}
