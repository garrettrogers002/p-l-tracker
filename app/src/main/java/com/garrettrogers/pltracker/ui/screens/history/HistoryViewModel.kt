package com.garrettrogers.pltracker.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garrettrogers.pltracker.data.model.Trade
import com.garrettrogers.pltracker.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class HistoryGroup(
    val monthYear: String,
    val totalPnl: BigDecimal,
    val trades: List<Trade>
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TradeRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.US)

    val historyGroups: StateFlow<List<HistoryGroup>> = repository.getClosedTrades()
        .map { trades ->
            trades
                .sortedByDescending { it.exitDate ?: 0L }
                .groupBy { trade ->
                    val date = Date(trade.exitDate ?: 0L)
                    dateFormat.format(date)
                }
                .map { (monthYear, monthTrades) ->
                    val totalPnl = monthTrades.fold(BigDecimal.ZERO) { acc, trade ->
                        acc.add(calculatePnl(trade))
                    }
                    HistoryGroup(monthYear, totalPnl, monthTrades)
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun calculatePnl(trade: Trade): BigDecimal {
        return try {
            if (trade.entryOptionPrice.isBlank() || trade.exitOptionPrice.isNullOrBlank()) {
                BigDecimal.ZERO
            } else {
                val entryObj = BigDecimal(trade.entryOptionPrice)
                val exitObj = BigDecimal(trade.exitOptionPrice)
                val qty = BigDecimal(trade.quantity)
                val multiplier = BigDecimal(100)
                
                // (Exit - Entry) * Qty * 100
                exitObj.subtract(entryObj).multiply(qty).multiply(multiplier)
            }
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }
}
