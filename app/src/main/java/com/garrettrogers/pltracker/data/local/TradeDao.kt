package com.garrettrogers.pltracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.garrettrogers.pltracker.data.model.Trade
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: Trade)

    @Update
    suspend fun updateTrade(trade: Trade)

    @Delete
    suspend fun deleteTrade(trade: Trade)

    @Query("SELECT * FROM trades WHERE isClosed = 0 ORDER BY entryDate DESC")
    fun getActiveTrades(): Flow<List<Trade>>

    @Query("SELECT * FROM trades WHERE isClosed = 1 ORDER BY exitDate DESC")
    fun getClosedTrades(): Flow<List<Trade>>

    @Query("SELECT * FROM trades WHERE id = :id")
    suspend fun getTradeById(id: Long): Trade?
    
    @Query("SELECT * FROM trades WHERE ticker = :ticker AND isClosed = 1 ORDER BY exitDate ASC")
    fun getClosedTradesByTicker(ticker: String): Flow<List<Trade>>
}
