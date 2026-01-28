package com.garrettrogers.pltracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import com.garrettrogers.pltracker.data.model.Trade

@Database(
    entities = [Trade::class, PortfolioSnapshot::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tradeDao(): TradeDao
    abstract fun portfolioDao(): PortfolioDao
}
