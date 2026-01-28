package com.garrettrogers.pltracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import kotlinx.coroutines.flow.Flow
import com.garrettrogers.pltracker.data.model.AccountTransaction

@Dao
interface PortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: PortfolioSnapshot)

    @androidx.room.Delete
    suspend fun deleteSnapshot(snapshot: PortfolioSnapshot)

    @Query("SELECT * FROM portfolio_snapshots ORDER BY date DESC")
    fun getSnapshots(): Flow<List<PortfolioSnapshot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: AccountTransaction)

    @androidx.room.Delete
    suspend fun deleteTransaction(transaction: AccountTransaction)

    @Query("SELECT * FROM account_transactions ORDER BY date DESC")
    fun getTransactions(): Flow<List<AccountTransaction>>
}
