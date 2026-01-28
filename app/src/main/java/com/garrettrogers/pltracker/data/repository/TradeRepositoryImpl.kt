package com.garrettrogers.pltracker.data.repository

import com.garrettrogers.pltracker.data.local.PortfolioDao
import com.garrettrogers.pltracker.data.local.TradeDao
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import com.garrettrogers.pltracker.data.model.Trade
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TradeRepositoryImpl @Inject constructor(
    private val tradeDao: TradeDao,
    private val portfolioDao: PortfolioDao
) : TradeRepository {

    override fun getActiveTrades(): Flow<List<Trade>> = tradeDao.getActiveTrades()

    override fun getClosedTrades(): Flow<List<Trade>> = tradeDao.getClosedTrades()

    override suspend fun getTradeById(id: Long): Trade? = tradeDao.getTradeById(id)

    override suspend fun insertTrade(trade: Trade) = tradeDao.insertTrade(trade)

    override suspend fun updateTrade(trade: Trade) = tradeDao.updateTrade(trade)

    override suspend fun deleteTrade(trade: Trade) = tradeDao.delete(trade)

    override fun getSnapshots(): Flow<List<PortfolioSnapshot>> = portfolioDao.getSnapshots()

    override suspend fun logSnapshot(snapshot: PortfolioSnapshot) = portfolioDao.insertSnapshot(snapshot)

    override fun getClosedTradesByTicker(ticker: String): Flow<List<Trade>> = tradeDao.getClosedTradesByTicker(ticker)
}
