package com.example.healthcare.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.healthcare.ui.navigation.AppRoutes

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(AppRoutes.SISTEM_PAKAR_SCREEN, Icons.Default.MedicalServices, "Diagnosa"),
    BottomNavItem(AppRoutes.HOME_SCREEN, Icons.Default.Home, "Home"),
    BottomNavItem(AppRoutes.ARTIKEL_SCREEN, Icons.AutoMirrored.Filled.Article, "Artikel"),
    BottomNavItem(AppRoutes.PROFILE_SCREEN, Icons.Default.Person, "Profil")
)

@Composable

fun StandardBottomNavBarWithLabel(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 10.dp,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route

                BottomNavItemWithLabel(
                    item = item,
                    isSelected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun BottomNavItemWithLabel(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconTint by animateColorAsState(
        targetValue = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(250),
        label = "nav_icon_tint"
    )

    Column(
        modifier = Modifier.padding(top = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        IconButton(
            onClick = onClick,
            modifier = Modifier.size(36.dp) // sama seperti versi kamu
        ) {
            Box(contentAlignment = Alignment.Center) {

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(48.dp) // sama seperti versi floating
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                CircleShape
                            )
                    )
                }

                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp) // ikon sama persis
                )
            }
        }

        Text(
            text = item.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp // sama seperti versi floating yang kamu pakai
            ),
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}
