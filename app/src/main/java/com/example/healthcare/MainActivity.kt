package com.example.healthcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState // Import penting untuk state
import androidx.compose.runtime.getValue     // Import penting untuk by
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel // Import untuk Hilt ViewModel
import com.example.healthcare.ui.navigation.AppNavigation
import com.example.healthcare.ui.theme.HealthcareTheme
import com.example.healthcare.viewmodel.ThemeViewModel // Import ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // PENTING: Enable Edge to Edge agar konten bisa sampai ke status bar
        enableEdgeToEdge()

        setContent {
            // 1. Ambil ViewModel Tema
            val themeViewModel: ThemeViewModel = hiltViewModel()

            // 2. Ambil State Tema (0=System, 1=Light, 2=Dark) secara real-time
            val themeMode by themeViewModel.themeState.collectAsState()

            // 3. Pasang Tema dengan Mode yang dipilih user
            HealthcareTheme(themeMode = themeMode) {

                // Surface pembungkus utama (Background global)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}