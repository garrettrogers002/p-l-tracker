package com.garrettrogers.pltracker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: PortfolioSnapshot)

    @Query("SELECT * FROM portfolio_snapshots ORDER BY date ASC")
    fun getSnapshots(): Flow<List<PortfolioSnapshot>>
}
