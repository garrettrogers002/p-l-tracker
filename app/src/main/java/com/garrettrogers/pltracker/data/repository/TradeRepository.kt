package com.garrettrogers.pltracker.data.repository

import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import com.garrettrogers.pltracker.data.model.Trade
import com.garrettrogers.pltracker.data.model.AccountTransaction
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
    suspend fun deleteSnapshot(snapshot: PortfolioSnapshot)
    
    fun getClosedTradesByTicker(ticker: String): Flow<List<Trade>>
    

    suspend fun getAllTrades(): List<Trade>

    fun getTransactions(): Flow<List<AccountTransaction>>
    suspend fun logTransaction(transaction: AccountTransaction)
    suspend fun deleteTransaction(transaction: AccountTransaction)
}
