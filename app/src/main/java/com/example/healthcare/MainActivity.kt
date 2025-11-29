package com.example.healthcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthcare.ui.navigation.AppNavigation
import com.example.healthcare.ui.theme.HealthcareTheme
import com.example.healthcare.viewmodel.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen // <-- IMPORT WAJIB

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // 1. PASANG SPLASH SCREEN API (WAJIB: Dipanggil SEBELUM super.onCreate)
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()

            // State Tema
            val themeMode by themeViewModel.themeState.collectAsState()

            // Pasang Tema dengan Mode yang dipilih user
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