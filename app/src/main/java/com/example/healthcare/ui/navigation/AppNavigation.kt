package com.example.healthcare.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.* // Import penting untuk State
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.healthcare.ui.components.FloatingBottomNavBar
import com.example.healthcare.ui.components.LoginRequiredDialog // Pastikan file ini ada di ui/components/

// Screens Import
import com.example.healthcare.ui.screens.home.HomeScreen
import com.example.healthcare.ui.screens.login.LoginScreen
import com.example.healthcare.ui.screens.register.RegisterScreen
import com.example.healthcare.ui.screens.artikel.ArtikelScreen
import com.example.healthcare.ui.screens.artikel.ReadArtikelScreen
import com.example.healthcare.ui.screens.sistempakar.SistemPakarScreen
import com.example.healthcare.ui.screens.sistempakar.GejalaScreen
import com.example.healthcare.ui.screens.sistempakar.KondisiScreen
import com.example.healthcare.ui.screens.sistempakar.DetailScreen
import com.example.healthcare.ui.profile.ProfileScreen
import com.example.healthcare.ui.screens.ForgotPassword.ForgotPasswordScreen

import com.example.healthcare.viewmodel.SistemPakarViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    // State untuk Popup Login
    var showLoginDialog by remember { mutableStateOf(false) }

    // --- FUNGSI NAVIGASI AMAN (Untuk tombol di Home) ---
    // Kalau User Login -> Jalan. Kalau Guest -> Muncul Popup.
    fun checkAuthAndNavigate(action: () -> Unit) {
        if (auth.currentUser != null) {
            action()
        } else {
            showLoginDialog = true
        }
    }

    // Fungsi Logout/Login Ulang dari Popup
    fun navigateToLogin() {
        showLoginDialog = false
        navController.navigate(AppRoutes.LOGIN_SCREEN) {
            popUpTo(AppRoutes.HOME_SCREEN) { inclusive = true }
        }
    }

    // --- RENDER POPUP JIKA STATE TRUE ---
    if (showLoginDialog) {
        LoginRequiredDialog(
            onDismissRequest = { showLoginDialog = false },
            onConfirmClick = { navigateToLogin() }
        )
    }

    // --- SCREEN GUARD (Untuk memproteksi Halaman Penuh) ---
    // Digunakan di Profile, Artikel, dan Sistem Pakar
    @Composable
    fun LoginGuardScreen(content: @Composable () -> Unit) {
        if (auth.currentUser != null) {
            content()
        } else {
            // Jika Guest memaksa masuk (misal lewat BottomBar), tendang balik & munculkan popup
            LaunchedEffect(Unit) {
                showLoginDialog = true
                navController.popBackStack() // Kembali ke layar sebelumnya (biasanya Home)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN_SCREEN
    ) {
        // == AUTH ROUTES ==
        composable(AppRoutes.LOGIN_SCREEN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(AppRoutes.HOME_SCREEN) { popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true } } },
                onRegisterClick = { navController.navigate(AppRoutes.REGISTER_SCREEN) },
                onForgotPasswordClick = { navController.navigate(AppRoutes.FORGOT_PASSWORD_SCREEN) },

                // AKSI TOMBOL TAMU: Masuk ke Home tanpa Login
                onGuestClick = {
                    navController.navigate(AppRoutes.HOME_SCREEN) { popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true } }
                }
            )
        }
        composable(AppRoutes.REGISTER_SCREEN) { RegisterScreen(onRegisterSuccess = { navController.popBackStack() }, onBackClick = { navController.popBackStack() }) }
        composable(AppRoutes.FORGOT_PASSWORD_SCREEN) { ForgotPasswordScreen(onBackClick = { navController.popBackStack() }) }

        // == HOME SCREEN (PUBLIC - BISA DIAKSES TAMU) ==
        // Tidak pakai LoginGuardScreen di sini
        composable(AppRoutes.HOME_SCREEN) {
            Scaffold(bottomBar = { FloatingBottomNavBar(navController) }) { paddingValues ->
                HomeScreen(
                    modifier = Modifier.padding(paddingValues),

                    // PASANG PENJAGA DI SINI (Saat tombol diklik)
                    onSistemPakarClick = {
                        checkAuthAndNavigate { navController.navigate(AppRoutes.SISTEM_PAKAR_SCREEN) }
                    },
                    onArtikelClick = {
                        checkAuthAndNavigate { navController.navigate(AppRoutes.ARTIKEL_SCREEN) }
                    }
                )
            }
        }

        // == PROFILE (PRIVATE) ==
        composable(AppRoutes.PROFILE_SCREEN) {
            LoginGuardScreen { // <-- Proteksi Halaman
                Scaffold(bottomBar = { FloatingBottomNavBar(navController) }) { paddingValues ->
                    ProfileScreen(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        onLogoutClick = { auth.signOut(); navigateToLogin() }
                    )
                }
            }
        }

        // == ARTIKEL (PRIVATE) ==
        composable(AppRoutes.ARTIKEL_SCREEN) {
            LoginGuardScreen {
                Scaffold(bottomBar = { FloatingBottomNavBar(navController) }) { paddingValues ->
                    ArtikelScreen(modifier = Modifier.padding(paddingValues), onBackClick = { navController.popBackStack() }, onArtikelDetailClick = { artikelId -> navController.navigate("${AppRoutes.ARTIKEL_DETAIL_ROUTE}/$artikelId") })
                }
            }
        }
        composable(route = AppRoutes.ARTIKEL_DETAIL_SCREEN, arguments = listOf(navArgument(AppRoutes.ARTIKEL_DETAIL_ARG_ID) { type = NavType.StringType })) { backStackEntry ->
            LoginGuardScreen {
                val artikelId = backStackEntry.arguments?.getString(AppRoutes.ARTIKEL_DETAIL_ARG_ID)
                ReadArtikelScreen(itemId = artikelId, onBackClick = { navController.popBackStack() })
            }
        }

        // ==========================================================
        // == SISTEM PAKAR (PRIVATE & SHARED VM) ==
        // ==========================================================

        composable(AppRoutes.SISTEM_PAKAR_SCREEN) {
            LoginGuardScreen { // <-- Proteksi Halaman
                Scaffold(bottomBar = { FloatingBottomNavBar(navController) }) { paddingValues ->
                    SistemPakarScreen(
                        modifier = Modifier.padding(paddingValues),
                        onMulaiClick = { navController.navigate("sistem_pakar_flow") },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        // FLOW DIAGNOSA
        navigation(startDestination = AppRoutes.GEJALA_SCREEN, route = "sistem_pakar_flow") {

            // SCREEN 1: GEJALA
            composable(AppRoutes.GEJALA_SCREEN) { backStackEntry ->
                LoginGuardScreen {
                    val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("sistem_pakar_flow") }
                    val sharedViewModel = hiltViewModel<SistemPakarViewModel>(parentEntry)

                    GejalaScreen(
                        viewModel = sharedViewModel,
                        onBackClick = { navController.popBackStack() },
                        onLanjutClick = { navController.navigate(AppRoutes.KONDISI_SCREEN) }
                    )
                }
            }

            // SCREEN 2: KONDISI
            composable(AppRoutes.KONDISI_SCREEN) { backStackEntry ->
                LoginGuardScreen {
                    val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("sistem_pakar_flow") }
                    val sharedViewModel = hiltViewModel<SistemPakarViewModel>(parentEntry)

                    KondisiScreen(
                        viewModel = sharedViewModel,
                        onBackClick = {
                            sharedViewModel.clearDiagnosa()
                            navController.popBackStack()
                        },
                        onLanjutClick = {
                            val hasilId = "result_id"
                            navController.navigate("${AppRoutes.PAKAR_DETAIL_ROUTE}/$hasilId")
                        }
                    )
                }
            }

            // SCREEN 3: DETAIL (ARTIKEL + SAVE HISTORY)
            composable(
                route = AppRoutes.PAKAR_DETAIL_SCREEN,
                arguments = listOf(navArgument(AppRoutes.PAKAR_DETAIL_ARG_ID) { type = NavType.StringType })
            ) { backStackEntry ->
                LoginGuardScreen {
                    val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("sistem_pakar_flow") }
                    val sharedViewModel = hiltViewModel<SistemPakarViewModel>(parentEntry)
                    val pakarId = backStackEntry.arguments?.getString(AppRoutes.PAKAR_DETAIL_ARG_ID)

                    DetailScreen(
                        itemId = pakarId,
                        viewModel = sharedViewModel,
                        onBackClick = { navController.popBackStack() },

                        // --- LOGIKA SIMPAN HISTORY (SESUAI PERMINTAAN) ---
                        onSelesaiClick = {
                            // 1. Simpan ke Riwayat
                            sharedViewModel.saveResultToHistory()
                            // 2. Navigasi keluar
                            navController.popBackStack(route = AppRoutes.SISTEM_PAKAR_SCREEN, inclusive = false)
                        }
                    )
                }
            }
        }
    }
}