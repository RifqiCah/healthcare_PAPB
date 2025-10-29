package com.example.healthcare.ui.theme

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 🎨 Warna sesuai desain
private val LightColors = lightColorScheme(
    primary = Color(0xFF2196F3),       // Biru utama
    onPrimary = Color.White,
    secondary = Color(0xFF03A9F4),     // Biru muda
    onSecondary = Color.Black,
    background = Color(0xFFF9F9F9),
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color.Black,
    secondary = Color(0xFF81D4FA),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White
)

// 🔹 Helper biar bisa dapetin Activity dari Context
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? android.content.ContextWrapper)?.baseContext?.findActivity()
}

@Composable
fun HealthcareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    val context = LocalContext.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = context.findActivity()?.window
            window?.statusBarColor = colorScheme.primary.toArgb()
            if (window != null) {
                WindowCompat.getInsetsController(window, view)
                    .isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    // ✅ ini udah dalam @Composable context
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
