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
// InfoScreen DIHAPUS IMPORTNYA
import com.example.healthcare.ui.profile.ProfileScreen
import com.example.healthcare.ui.screens.ForgotPassword.ForgotPasswordScreen

import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    // 1. Navigasi dengan cek Login (Dipakai di onClick)
    fun navigateSecured(route: String) {
        if (auth.currentUser != null) {
            navController.navigate(route)
        } else {
            navController.navigate(AppRoutes.LOGIN_SCREEN)
        }
    }

    // 2. Screen Guard (Dipakai membungkus Halaman)
    @Composable
    fun SecuredScreen(content: @Composable () -> Unit) {
        if (auth.currentUser != null) {
            content()
        } else {
            LaunchedEffect(Unit) {
                navController.navigate(AppRoutes.LOGIN_SCREEN) {
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
                onRegisterSuccess = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // == HOME SCREEN ==
        composable(AppRoutes.HOME_SCREEN) {
            Scaffold(
                bottomBar = { FloatingBottomNavBar(navController) }
            ) { paddingValues ->
                HomeScreen(
                    modifier = Modifier.padding(paddingValues),
                    onSistemPakarClick = { navigateSecured(AppRoutes.SISTEM_PAKAR_SCREEN) },
                    onArtikelClick = { navigateSecured(AppRoutes.ARTIKEL_SCREEN) }
                )
            }
        }

        // == PROFILE ==
        composable(AppRoutes.PROFILE_SCREEN) {
            SecuredScreen {
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
            SecuredScreen {
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
            SecuredScreen {
                val artikelId = backStackEntry.arguments?.getString(AppRoutes.ARTIKEL_DETAIL_ARG_ID)
                ReadArtikelScreen(
                    itemId = artikelId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // == SISTEM PAKAR ==
        composable(AppRoutes.SISTEM_PAKAR_SCREEN) {
            SecuredScreen {
                Scaffold(bottomBar = { FloatingBottomNavBar(navController) }) { paddingValues ->
                    SistemPakarScreen(
                        modifier = Modifier.padding(paddingValues),
                        // UBAH DISINI: Langsung ke GejalaScreen
                        onMulaiClick = { navController.navigate(AppRoutes.GEJALA_SCREEN) },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        // HAPUS ROUTE INFO SCREEN DI SINI

        composable(AppRoutes.GEJALA_SCREEN) {
            SecuredScreen {
                GejalaScreen(
                    onBackClick = { navController.popBackStack() },
                    // Setelah Gejala -> Lanjut ke Kondisi (atau langsung Detail kalau mau skip kondisi)
                    onLanjutClick = { navController.navigate(AppRoutes.KONDISI_SCREEN) }
                )
            }
        }

        composable(AppRoutes.KONDISI_SCREEN) {
            SecuredScreen {
                KondisiScreen(
                    onBackClick = { navController.popBackStack() },
                    onLanjutClick = {
                        // Nanti di sini kita bisa kirim ID hasil diagnosa yang sebenarnya
                        val hasilId = "HasilDiagnosa_001"
                        navController.navigate("${AppRoutes.PAKAR_DETAIL_ROUTE}/$hasilId")
                    }
                )
            }
        }

        composable(
            route = AppRoutes.PAKAR_DETAIL_SCREEN,
            arguments = listOf(navArgument(AppRoutes.PAKAR_DETAIL_ARG_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            SecuredScreen {
                val pakarId = backStackEntry.arguments?.getString(AppRoutes.PAKAR_DETAIL_ARG_ID)
                DetailScreen(
                    itemId = pakarId,
                    onBackClick = { navController.popBackStack() },
                    onSelesaiClick = {
                        // Kembali ke Menu Awal Sistem Pakar (Reset Stack)
                        navController.popBackStack(route = AppRoutes.SISTEM_PAKAR_SCREEN, inclusive = false)
                    }
                )
            }
        }
    }
}