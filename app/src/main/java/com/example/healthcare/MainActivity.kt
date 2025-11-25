package com.example.healthcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.healthcare.ui.navigation.AppNavigation
import com.example.healthcare.ui.theme.HealthcareTheme
import android.util.Log


// 1. WAJIB IMPORT INI
import dagger.hilt.android.AndroidEntryPoint

// 2. WAJIB TAMBAHKAN ANOTASI INI DI ATAS CLASS
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            HealthcareTheme {
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