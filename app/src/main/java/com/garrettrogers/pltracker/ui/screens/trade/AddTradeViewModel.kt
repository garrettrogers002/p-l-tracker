package com.garrettrogers.pltracker.ui.screens.trade

import androidx.lifecycle.ViewModel
import com.garrettrogers.pltracker.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.garrettrogers.pltracker.data.model.Trade
import kotlinx.coroutines.launch

@HiltViewModel
class AddTradeViewModel @Inject constructor(
    private val repository: TradeRepository
) : ViewModel() {
    
    var ticker by mutableStateOf("")
    var entryDate by mutableStateOf(System.currentTimeMillis())
    var expirationDate by mutableStateOf(System.currentTimeMillis())
    var strikePrice by mutableStateOf("")
    var optionPrice by mutableStateOf("")
    var stockPrice by mutableStateOf("")
    var quantity by mutableStateOf("")

    fun saveTrade(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val trade = Trade(
                ticker = ticker,
                entryDate = entryDate,
                expirationDate = expirationDate,
                strikePrice = strikePrice,
                entryOptionPrice = optionPrice,
                entryStockPrice = stockPrice,
                quantity = quantity.toIntOrNull() ?: 1
            )
            repository.insertTrade(trade)
            onSuccess()
        }
    }
}
