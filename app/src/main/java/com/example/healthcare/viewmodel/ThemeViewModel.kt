package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.data.local.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    // Mengambil data tema secara real-time
    // 0 = System, 1 = Light, 2 = Dark
    val themeState: StateFlow<Int> = themeManager.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0 // Default Sistem
        )

    fun changeTheme(mode: Int) {
        viewModelScope.launch {
            themeManager.saveTheme(mode)
        }
    }
}