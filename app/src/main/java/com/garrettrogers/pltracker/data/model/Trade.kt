package com.garrettrogers.pltracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trades")
data class Trade(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ticker: String,
    val entryDate: Long, // Epoch millis
    val expirationDate: Long, // Epoch millis
    val strikePrice: String, // BigDecimal stored as String
    val entryOptionPrice: String, // BigDecimal stored as String
    val entryStockPrice: String, // BigDecimal stored as String
    val quantity: Int,
    val exitDate: Long? = null,
    val exitOptionPrice: String? = null,
    val exitStockPrice: String? = null,
    val optionType: String = "CALL", // "CALL" or "PUT"
    val isClosed: Boolean = false // Explicit flag for filtering
)
