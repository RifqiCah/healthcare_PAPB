// Di file /com/example/healthcare/MainActivity.kt
package com.example.healthcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
// 1. Import AppNavigation yang sudah kita buat
import com.example.healthcare.ui.navigation.AppNavigation
// 2. Import Theme aplikasi Anda
import com.example.healthcare.ui.theme.HealthcareTheme
// 3. Import untuk Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 3. Gunakan Theme aplikasi Anda
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

