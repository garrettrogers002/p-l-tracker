package com.garrettrogers.pltracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_transactions")
data class AccountTransaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val amount: String, // BigDecimal stored as String
    val type: String, // "DEPOSIT" or "WITHDRAWAL"
    val note: String = ""
)
