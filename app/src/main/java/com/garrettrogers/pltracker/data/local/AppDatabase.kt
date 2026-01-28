package com.garrettrogers.pltracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import com.garrettrogers.pltracker.data.model.Trade
import com.garrettrogers.pltracker.data.model.AccountTransaction

@Database(
    entities = [Trade::class, PortfolioSnapshot::class, AccountTransaction::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tradeDao(): TradeDao
    abstract fun portfolioDao(): PortfolioDao
}
