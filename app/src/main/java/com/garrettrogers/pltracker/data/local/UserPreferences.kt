package com.garrettrogers.pltracker.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    
    private object Keys {
        val REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        val REMINDER_TIME_HOUR = stringPreferencesKey("reminder_hour") // Keep simple string or int
        val REMINDER_TIME_MINUTE = stringPreferencesKey("reminder_minute")
    }

    val reminderEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[Keys.REMINDER_ENABLED] ?: false }

    val reminderHour: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[Keys.REMINDER_TIME_HOUR]?.toInt() ?: 16 } // Default 4

    val reminderMinute: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[Keys.REMINDER_TIME_MINUTE]?.toInt() ?: 15 } // Default 15

    suspend fun setReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[Keys.REMINDER_ENABLED] = enabled
        }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit { preferences ->
            preferences[Keys.REMINDER_TIME_HOUR] = hour.toString()
            preferences[Keys.REMINDER_TIME_MINUTE] = minute.toString()
        }
    }
}
