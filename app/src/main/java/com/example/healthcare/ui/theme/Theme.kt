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

// --- PALET WARNA (LIGHT MODE) ---
private val LightColors = lightColorScheme(
    primary = Color(0xFF00BCD4),        // Cyan Utama
    onPrimary = Color.White,            // Teks Putih di atas Cyan

    // Container Utama (Cyan Muda)
    primaryContainer = Color(0xFFE0F7FA),
    onPrimaryContainer = Color(0xFF006064),

    secondary = Color(0xFF1565C0),      // Biru Tua
    onSecondary = Color(0xFFFFFFFF),

    secondaryContainer = Color(0xFFD1E4FF),
    onSecondaryContainer = Color(0xFF001D36),

    background = Color(0xFFF5F9FA),     // Putih Sejuk (Latar Layar)
    onBackground = Color(0xFF191C1C),

    surface = Color(0xFFFFFFFF),        // Background Card biasa
    onSurface = Color(0xFF191C1C),

    // Warna Khusus Navbar
    surfaceContainer = Color(0xFFFFFFFF), // Warna Kapsul Navbar (Putih Bersih)
    onSurfaceVariant = Color(0xFF70797C)  // Warna Ikon Tidak Dipilih
)

// --- PALET WARNA (DARK MODE) ---
private val DarkColors = darkColorScheme(
    primary = Color(0xFF80DEEA),
    onPrimary = Color(0xFF00363D),

    primaryContainer = Color(0xFF004D40),
    onPrimaryContainer = Color(0xFFE0F7FA),

    secondary = Color(0xFF90CAF9),
    onSecondary = Color(0xFF003258),

    background = Color(0xFF121212),     // Hitam Pekat
    onBackground = Color(0xFFE1E3E3),

    surface = Color(0xFF1E1E1E),        // Background Card (Abu Gelap)
    onSurface = Color(0xFFE1E3E3),

    // Warna Khusus Navbar Dark Mode
    surfaceContainer = Color(0xFF252B2D), // Warna Kapsul Navbar (Abu-Cyan Gelap)
    onSurfaceVariant = Color(0xFFB0BEC5)  // Warna Ikon Tidak Dipilih
)

// 🔹 Helper biar bisa dapetin Activity dari Context
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? android.content.ContextWrapper)?.baseContext?.findActivity()
}

@Composable
fun HealthcareTheme(
    // Parameter baru: themeMode (0=System, 1=Light, 2=Dark)
    // Default 0 agar kalau dipanggil tanpa parameter tetap jalan (ikut sistem)
    themeMode: Int = 0,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Logika penentuan Gelap/Terang berdasarkan pilihan User
    val darkTheme = when (themeMode) {
        1 -> false // Paksa Light
        2 -> true  // Paksa Dark
        else -> isSystemInDarkTheme() // Ikut Sistem (0)
    }

    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    val context = LocalContext.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = context.findActivity()?.window
            // Ubah warna status bar mengikuti Primary
            window?.statusBarColor = colorScheme.primary.toArgb()

            if (window != null) {
                // Ikon status bar: Gelap di Light Mode, Terang di Dark Mode
                WindowCompat.getInsetsController(window, view)
                    .isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}