package com.example.healthcare.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthcare.ui.components.FloatingBottomNavBar

// Screens
import com.example.healthcare.ui.screens.home.HomeScreen
import com.example.healthcare.ui.screens.login.LoginScreen
import com.example.healthcare.ui.screens.register.RegisterScreen
import com.example.healthcare.ui.screens.artikel.ArtikelScreen
import com.example.healthcare.ui.screens.artikel.ReadArtikelScreen
import com.example.healthcare.ui.screens.sistempakar.SistemPakarScreen
import com.example.healthcare.ui.screens.sistempakar.GejalaScreen
import com.example.healthcare.ui.screens.sistempakar.KondisiScreen
import com.example.healthcare.ui.screens.sistempakar.DetailScreen
import com.example.healthcare.ui.screens.sistempakar.InfoScreen
import com.example.healthcare.ui.profile.ProfileScreen
import com.example.healthcare.ui.screens.ForgotPassword.ForgotPasswordScreen

import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    // ===============================================
    // 1. FUNGSI UNTUK CLICK (Logic Guest View di Homepage)
    // Digunakan saat user mengklik fitur yang memerlukan login dari HomeScreen
    // ===============================================
    fun navigateSecured(route: String) {
        if (auth.currentUser != null) {
            // Jika sudah login, navigasi ke tujuan
            navController.navigate(route)
        } else {
            // Jika belum login, navigasi paksa ke LoginScreen
            navController.navigate(AppRoutes.LOGIN_SCREEN)
            // Catatan: Anda mungkin ingin menambahkan Toast/Snackbar di HomeScreen
            // untuk memberi tahu pengguna bahwa login diperlukan.
        }
    }

    // ===============================================
    // 2. FUNGSI UNTUK SCREEN (Perlindungan Screen)
    // Digunakan untuk melindungi composable screen secara penuh
    // ===============================================
    @Composable
    fun SecuredScreen(content: @Composable () -> Unit) {
        if (auth.currentUser != null) {
            content()
        } else {
            // Jika belum login, lempar paksa ke Login
            LaunchedEffect(Unit) {
                navController.navigate(AppRoutes.LOGIN_SCREEN) {
                    // Membersihkan stack navigasi hingga HomeScreen (jika ada)
                    // agar pengguna tidak bisa kembali ke halaman sensitif
                    popUpTo(AppRoutes.HOME_SCREEN) { inclusive = true }
                }
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
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME_SCREEN) {
                        popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(AppRoutes.REGISTER_SCREEN) },
                onForgotPasswordClick = { navController.navigate(AppRoutes.FORGOT_PASSWORD_SCREEN) }
            )
        }

        composable(AppRoutes.REGISTER_SCREEN) {
            RegisterScreen(
                // Setelah register berhasil, kembali ke Login Screen
                onRegisterSuccess = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // == HOME SCREEN (Akses Guest Diizinkan) ==
        composable(AppRoutes.HOME_SCREEN) {
            Scaffold(
                bottomBar = { FloatingBottomNavBar(navController) }
            ) { paddingValues ->
                HomeScreen(
                    modifier = Modifier.padding(paddingValues),

                    // MENGGUNAKAN LOGIC GUEST VIEW:
                    // Panggilan ke navigateSecured() akan mengecek status login.
                    onSistemPakarClick = {
                        navigateSecured(AppRoutes.SISTEM_PAKAR_SCREEN)
                    },
                    onArtikelClick = {
                        navigateSecured(AppRoutes.ARTIKEL_SCREEN)
                    }
                )
            }
        }

        // ===============================================
        // == SEMUA ROUTE DI BAWAH INI DILINDUNGI SECUREDSCREEN
        // ===============================================

        // == PROFILE ==
        composable(AppRoutes.PROFILE_SCREEN) {
            SecuredScreen { // Perlindungan
                Scaffold(
                    bottomBar = { FloatingBottomNavBar(navController) }
                ) { paddingValues ->
                    ProfileScreen(
                        modifier = Modifier.padding(paddingValues),
                        navController = navController,
                        onLogoutClick = {
                            auth.signOut()
                            navController.navigate(AppRoutes.LOGIN_SCREEN) {
                                popUpTo(AppRoutes.HOME_SCREEN) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

        // == ARTIKEL ==
        composable(AppRoutes.ARTIKEL_SCREEN) {
            SecuredScreen { // Perlindungan
                Scaffold(
                    bottomBar = { FloatingBottomNavBar(navController) }
                ) { paddingValues ->
                    ArtikelScreen(
                        modifier = Modifier.padding(paddingValues),
                        onBackClick = { navController.popBackStack() },
                        onArtikelDetailClick = { artikelId ->
                            navController.navigate("${AppRoutes.ARTIKEL_DETAIL_ROUTE}/$artikelId")
                        }
                    )
                }
            }
        }

        composable(
            route = AppRoutes.ARTIKEL_DETAIL_SCREEN,
            arguments = listOf(navArgument(AppRoutes.ARTIKEL_DETAIL_ARG_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            SecuredScreen { // Perlindungan
                val artikelId = backStackEntry.arguments?.getString(AppRoutes.ARTIKEL_DETAIL_ARG_ID)
                ReadArtikelScreen(
                    itemId = artikelId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // == SISTEM PAKAR ==
        composable(AppRoutes.SISTEM_PAKAR_SCREEN) {
            SecuredScreen { // Perlindungan
                Scaffold(bottomBar = { FloatingBottomNavBar(navController) }) { paddingValues ->
                    SistemPakarScreen(
                        modifier = Modifier.padding(paddingValues),
                        onMulaiClick = { navController.navigate(AppRoutes.INFO_SCREEN) },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        composable(AppRoutes.INFO_SCREEN) {
            SecuredScreen { // Perlindungan
                InfoScreen(
                    onBackClick = { navController.popBackStack() },
                    onLanjutClick = { navController.navigate(AppRoutes.GEJALA_SCREEN) }
                )
            }
        }

        composable(AppRoutes.GEJALA_SCREEN) {
            SecuredScreen { // Perlindungan
                GejalaScreen(
                    onBackClick = { navController.popBackStack() },
                    onLanjutClick = { navController.navigate(AppRoutes.KONDISI_SCREEN) }
                )
            }
        }

        composable(AppRoutes.KONDISI_SCREEN) {
            SecuredScreen { // Perlindungan
                KondisiScreen(
                    onBackClick = { navController.popBackStack() },
                    onLanjutClick = {
                        val hasilId = "HasilDiagnosa_001" // Logic dummy ID
                        navController.navigate("${AppRoutes.PAKAR_DETAIL_ROUTE}/$hasilId")
                    }
                )
            }
        }

        composable(
            route = AppRoutes.PAKAR_DETAIL_SCREEN,
            arguments = listOf(navArgument(AppRoutes.PAKAR_DETAIL_ARG_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            SecuredScreen { // Perlindungan
                val pakarId = backStackEntry.arguments?.getString(AppRoutes.PAKAR_DETAIL_ARG_ID)
                DetailScreen(
                    itemId = pakarId,
                    onBackClick = { navController.popBackStack() },
                    onSelesaiClick = {
                        navController.popBackStack(route = AppRoutes.SISTEM_PAKAR_SCREEN, inclusive = false)
                    }
                )
            }
        }
    }
}