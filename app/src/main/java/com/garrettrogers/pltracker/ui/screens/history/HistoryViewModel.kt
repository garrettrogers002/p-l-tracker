package com.garrettrogers.pltracker.ui.screens.history

import androidx.lifecycle.ViewModel
import com.garrettrogers.pltracker.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import com.garrettrogers.pltracker.data.model.Trade
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TradeRepository
) : ViewModel() {
    
    val closedTrades: StateFlow<List<Trade>> = repository.getClosedTrades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
