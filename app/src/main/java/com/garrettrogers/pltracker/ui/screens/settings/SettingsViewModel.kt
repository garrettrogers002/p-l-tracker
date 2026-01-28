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

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val application: Application
) : ViewModel() {

    val reminderEnabled = userPreferences.reminderEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val reminderHour = userPreferences.reminderHour
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 16)

    val reminderMinute = userPreferences.reminderMinute
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 15)

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
