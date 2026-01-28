package com.garrettrogers.pltracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolio_snapshots")
data class PortfolioSnapshot(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val totalValue: String // BigDecimal stored as String
)
