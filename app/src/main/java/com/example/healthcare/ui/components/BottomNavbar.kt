package com.example.healthcare.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// Data class untuk item navbar
data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

// Daftar item navbar
val bottomNavItems = listOf(

    BottomNavItem(
        route = "sistem_pakar",
        icon = Icons.Default.MedicalServices,
        label = "Sistem Pakar"
    ),
    BottomNavItem(
        route = "home",
        icon = Icons.Default.Home,
        label = "Home"
    ),
    BottomNavItem(
        route = "artikel",
        icon = Icons.Default.Article,
        label = "Artikel"
    )
)

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to start destination
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Avoid multiple copies
                        launchSingleTop = true
                        // Restore state when reselecting
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(text = item.label)
                }
            )
        }
    }
}