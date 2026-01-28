package com.garrettrogers.pltracker.data.repository

import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import com.garrettrogers.pltracker.data.model.Trade
import kotlinx.coroutines.flow.Flow

interface TradeRepository {
    fun getActiveTrades(): Flow<List<Trade>>
    fun getClosedTrades(): Flow<List<Trade>>
    suspend fun getTradeById(id: Long): Trade?
    suspend fun insertTrade(trade: Trade)
    suspend fun updateTrade(trade: Trade)
    suspend fun deleteTrade(trade: Trade)
    
    fun getSnapshots(): Flow<List<PortfolioSnapshot>>
    suspend fun logSnapshot(snapshot: PortfolioSnapshot)
    
    fun getClosedTradesByTicker(ticker: String): Flow<List<Trade>>
}
