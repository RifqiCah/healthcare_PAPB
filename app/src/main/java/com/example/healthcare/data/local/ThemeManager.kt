package com.example.healthcare.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Buat DataStore
val Context.dataStore by preferencesDataStore(name = "settings")

class ThemeManager(private val context: Context) {

    companion object {
        val THEME_KEY = intPreferencesKey("theme_mode")
    }

    val themeFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[THEME_KEY] ?: 0
        }

    suspend fun saveTheme(mode: Int) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = mode
        }
    }
}
