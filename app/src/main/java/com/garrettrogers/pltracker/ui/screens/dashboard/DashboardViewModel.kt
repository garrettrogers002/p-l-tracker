package com.garrettrogers.pltracker.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.garrettrogers.pltracker.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.lifecycle.viewModelScope
import com.garrettrogers.pltracker.data.model.Trade
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TradeRepository
) : ViewModel() {
    
    val activeTrades: StateFlow<List<Trade>> = repository.getActiveTrades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val snapshots: StateFlow<List<PortfolioSnapshot>> = repository.getSnapshots()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
