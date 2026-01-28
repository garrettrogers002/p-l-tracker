package com.garrettrogers.pltracker.di

import android.content.Context
import androidx.room.Room
import com.garrettrogers.pltracker.data.local.AppDatabase
import com.garrettrogers.pltracker.data.local.PortfolioDao
import com.garrettrogers.pltracker.data.local.TradeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pl_tracker_db"
        ).fallbackToDestructiveMigration() // For simplicity during dev
         .build()
    }

    @Provides
    @Singleton
    fun provideTradeDao(database: AppDatabase): TradeDao {
        return database.tradeDao()
    }

    @Provides
    @Singleton
    fun providePortfolioDao(database: AppDatabase): PortfolioDao {
        return database.portfolioDao()
    }
}
