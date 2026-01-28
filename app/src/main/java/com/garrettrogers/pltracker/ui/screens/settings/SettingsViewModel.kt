package com.garrettrogers.pltracker.ui.screens.settings

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.garrettrogers.pltracker.data.local.UserPreferences
import com.garrettrogers.pltracker.work.PortfolioReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.garrettrogers.pltracker.data.repository.TradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val repository: TradeRepository,
    private val application: Application
) : ViewModel() {

    val reminderEnabled = userPreferences.reminderEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val reminderHour = userPreferences.reminderHour
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 16)

    val reminderMinute = userPreferences.reminderMinute
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 15)

    private val _exportUri = MutableStateFlow<Uri?>(null)
    val exportUri = _exportUri.asStateFlow()

    fun clearExportUri() {
        _exportUri.value = null
    }

    fun exportCsv() {
        viewModelScope.launch {
            val trades = repository.getAllTrades()
            
            // Generate CSV Content
            // Format: SYMBOL | TYPE | EXPIRY | STRIKE | PREMIUM | QUANTITY 
            // Extra: PNL, ENTRY_DATE, EXIT_DATE
            val sb = StringBuilder()
            sb.append("SYMBOL,TYPE,EXPIRY,STRIKE,PREMIUM,QUANTITY,ENTRY_DATE,EXIT_DATE,EXIT_PREMIUM,PNL\n")
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            
            trades.forEach { trade ->
                val type = trade.optionType
                val expiry = dateFormat.format(Date(trade.expirationDate))
                val entryDate = dateFormat.format(Date(trade.entryDate))
                val exitDate = trade.exitDate?.let { dateFormat.format(Date(it)) } ?: ""
                val pnl = calculatePnl(trade)
                
                sb.append(
                    "${trade.ticker},$type,$expiry,${trade.strikePrice},${trade.entryOptionPrice},${trade.quantity},$entryDate,$exitDate,${trade.exitOptionPrice ?: ""},$pnl\n"
                )
            }
            
            // Save to File
            val file = withContext(Dispatchers.IO) {
                val exportDir = File(application.cacheDir, "exports")
                if (!exportDir.exists()) exportDir.mkdirs()
                val file = File(exportDir, "pl_tracker_export.csv")
                file.writeText(sb.toString())
                file
            }
            
            // Get URI
            val uri = FileProvider.getUriForFile(
                application,
                "${application.packageName}.provider",
                file
            )
            _exportUri.value = uri
        }
    }

    private fun calculatePnl(trade: com.garrettrogers.pltracker.data.model.Trade): String {
         return try {
            if (trade.entryOptionPrice.isBlank() || trade.exitOptionPrice.isNullOrBlank()) {
                "0"
            } else {
                val entryObj = java.math.BigDecimal(trade.entryOptionPrice)
                val exitObj = java.math.BigDecimal(trade.exitOptionPrice)
                val qty = java.math.BigDecimal(trade.quantity)
                val multiplier = java.math.BigDecimal(100)
                exitObj.subtract(entryObj).multiply(qty).multiply(multiplier).toPlainString()
            }
        } catch (e: Exception) {
            "0"
        }
    }

    fun setReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setReminderEnabled(enabled)
            if (enabled) {
                scheduleWorker()
            } else {
                cancelWorker()
            }
        }
    }

    fun setReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            userPreferences.setReminderTime(hour, minute)
            // Reschedule if enabled
            if (reminderEnabled.value) {
                scheduleWorker(hour, minute)
            }
        }
    }

    private fun scheduleWorker(hour: Int = reminderHour.value, minute: Int = reminderMinute.value) {
        val workManager = WorkManager.getInstance(application)
        
        // Calculate initial delay
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = target.timeInMillis - now.timeInMillis
        
        val request = PeriodicWorkRequestBuilder<PortfolioReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()
            
        workManager.enqueueUniquePeriodicWork(
            "portfolio_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    private fun cancelWorker() {
        WorkManager.getInstance(application).cancelUniqueWork("portfolio_reminder")
    }
}
