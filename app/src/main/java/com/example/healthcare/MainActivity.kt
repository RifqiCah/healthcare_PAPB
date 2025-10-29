package com.example.healthcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.healthcare.ui.screens.sistempakar.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SistemPakarApp()
        }
    }
}

@Composable
fun SistemPakarApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "info"
    ) {
        composable("info") {
            InfoScreen(
                onNext = { navController.navigate("gejala") }
            )
        }
        composable("gejala") {
            GejalaScreen(
                onNext = { navController.navigate("kondisi") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("kondisi") {
            KondisiScreen(
                onNext = { navController.navigate("detail") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("detail") {
            DetailScreen(
                onNext = { /* selesai atau ke halaman lain */ },
                onBack = { navController.popBackStack() }
            )
        }
    }
}