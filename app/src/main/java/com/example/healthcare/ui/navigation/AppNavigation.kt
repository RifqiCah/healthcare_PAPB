package com.example.healthcare.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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

    // ======================================
    // FUNCTION GUARD UNTUK MENGUNCI FITUR
    // ======================================
    @Composable
    fun requireLogin(action: @Composable () -> Unit) {
        if (auth.currentUser != null) {
            action() // user sudah login → lanjut
        } else {
            navController.navigate(AppRoutes.LOGIN_SCREEN)
        }
    }

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN_SCREEN
    ) {

        // ======================================
        // == AUTH ROUTES
        // ======================================

        composable(AppRoutes.LOGIN_SCREEN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME_SCREEN) {
                        popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppRoutes.REGISTER_SCREEN)
                },
                onForgotPasswordClick = {
                    navController.navigate(AppRoutes.FORGOT_PASSWORD_SCREEN)
                }
            )
        }

        composable(AppRoutes.REGISTER_SCREEN) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // ======================================
        // == HOME SCREEN (BOLEH UNTUK GUEST)
        // ======================================

        composable(AppRoutes.HOME_SCREEN) {
            Scaffold(
                bottomBar = {
                    FloatingBottomNavBar(
                        navController = navController,
                    )
                }
            ) { paddingValues ->
                HomeScreen(
                    modifier = Modifier.padding(paddingValues),

                    // FITUR DI KUNCI → requireLogin
                    onSistemPakarClick = {
                        requireLogin {
                            navController.navigate(AppRoutes.SISTEM_PAKAR_SCREEN)
                        }
                    },
                    onArtikelClick = {
                        requireLogin {
                            navController.navigate(AppRoutes.ARTIKEL_SCREEN)
                        }
                    }
                )
            }
        }

        // ======================================
        // == PROFILE (HARUS LOGIN)
        // ======================================

        composable(AppRoutes.PROFILE_SCREEN) {

            requireLogin {
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

        // ======================================
        // == ARTIKEL SCREEN (HARUS LOGIN)
        // ======================================

        composable(AppRoutes.ARTIKEL_SCREEN) {

            requireLogin {
                Scaffold(
                    bottomBar = { FloatingBottomNavBar(navController) }
                ) { paddingValues ->
                    ArtikelScreen(
                        modifier = Modifier.padding(paddingValues),
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onArtikelDetailClick = { artikelId ->
                            navController.navigate("${AppRoutes.ARTIKEL_DETAIL_ROUTE}/$artikelId")
                        }
                    )
                }
            }
        }

        composable(
            route = AppRoutes.ARTIKEL_DETAIL_SCREEN,
            arguments = listOf(
                navArgument(AppRoutes.ARTIKEL_DETAIL_ARG_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            requireLogin {
                val artikelId = backStackEntry.arguments?.getString(AppRoutes.ARTIKEL_DETAIL_ARG_ID)
                ReadArtikelScreen(
                    itemId = artikelId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // ======================================
        // == SISTEM PAKAR (HARUS LOGIN)
        // ======================================

        composable(AppRoutes.SISTEM_PAKAR_SCREEN) {

            requireLogin {
                Scaffold(bottomBar = { FloatingBottomNavBar(navController) }) { paddingValues ->
                    SistemPakarScreen(
                        modifier = Modifier.padding(paddingValues),
                        onMulaiClick = {
                            navController.navigate(AppRoutes.INFO_SCREEN)
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        composable(AppRoutes.INFO_SCREEN) {

            requireLogin {
                InfoScreen(
                    onBackClick = { navController.popBackStack() },
                    onLanjutClick = {
                        navController.navigate(AppRoutes.GEJALA_SCREEN)
                    }
                )
            }
        }

        composable(AppRoutes.GEJALA_SCREEN) {

            requireLogin {
                GejalaScreen(
                    onBackClick = { navController.popBackStack() },
                    onLanjutClick = {
                        navController.navigate(AppRoutes.KONDISI_SCREEN)
                    }
                )
            }
        }

        composable(AppRoutes.KONDISI_SCREEN) {

            requireLogin {
                KondisiScreen(
                    onBackClick = { navController.popBackStack() },
                    onLanjutClick = {
                        val hasilId = "HasilDiagnosa_001"
                        navController.navigate("${AppRoutes.PAKAR_DETAIL_ROUTE}/$hasilId")
                    }
                )
            }
        }

        composable(
            route = AppRoutes.PAKAR_DETAIL_SCREEN,
            arguments = listOf(
                navArgument(AppRoutes.PAKAR_DETAIL_ARG_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->

            requireLogin {
                val pakarId = backStackEntry.arguments?.getString(AppRoutes.PAKAR_DETAIL_ARG_ID)

                DetailScreen(
                    itemId = pakarId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSelesaiClick = {
                        navController.popBackStack(
                            route = AppRoutes.SISTEM_PAKAR_SCREEN,
                            inclusive = false
                        )
                    }
                )
            }
        }
    }
}