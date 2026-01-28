package com.garrettrogers.pltracker.ui.screens.analysis

import androidx.lifecycle.ViewModel
import com.garrettrogers.pltracker.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.garrettrogers.pltracker.data.model.PortfolioSnapshot
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val repository: TradeRepository
) : ViewModel() {
    
    val snapshots: StateFlow<List<PortfolioSnapshot>> = repository.getSnapshots()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    var snapshotValue by mutableStateOf("")

    fun logSnapshot() {
        val value = snapshotValue.toDoubleOrNull() ?: return
        viewModelScope.launch {
            val snapshot = PortfolioSnapshot(
                date = System.currentTimeMillis(),
                totalValue = snapshotValue
            )
            repository.logSnapshot(snapshot)
            snapshotValue = ""
        }
    }
}
