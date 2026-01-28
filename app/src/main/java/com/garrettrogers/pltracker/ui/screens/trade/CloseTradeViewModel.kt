package com.garrettrogers.pltracker.ui.screens.trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.garrettrogers.pltracker.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.garrettrogers.pltracker.data.model.Trade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CloseTradeViewModel @Inject constructor(
    private val repository: TradeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    private val tradeId: Long = checkNotNull(savedStateHandle["tradeId"])
    
    private val _trade = MutableStateFlow<Trade?>(null)
    val trade: StateFlow<Trade?> = _trade.asStateFlow()
    
    var exitDate by mutableStateOf(System.currentTimeMillis())
    var exitOptionPrice by mutableStateOf("")
    var exitStockPrice by mutableStateOf("")
    
    init {
        viewModelScope.launch {
            _trade.value = repository.getTradeById(tradeId)
        }
    }
    
    fun closeTrade(onSuccess: () -> Unit) {
        val currentTrade = _trade.value ?: return
        viewModelScope.launch {
            val updatedTrade = currentTrade.copy(
                exitDate = exitDate,
                exitOptionPrice = exitOptionPrice,
                exitStockPrice = exitStockPrice,
                isClosed = true
            )
            repository.updateTrade(updatedTrade)
            onSuccess()
        }
    }

    fun deleteTrade(onSuccess: () -> Unit) {
        val currentTrade = _trade.value ?: return
        viewModelScope.launch {
            repository.deleteTrade(currentTrade)
            onSuccess()
        }
    }
}
