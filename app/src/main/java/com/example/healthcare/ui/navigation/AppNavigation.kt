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

// --- Import semua Screen Anda di sini ---
import com.example.healthcare.ui.screens.home.HomeScreen
import com.example.healthcare.ui.screens.login.LoginScreen
import com.example.healthcare.ui.screens.register.RegisterScreen
import com.example.healthcare.ui.screens.artikel.ArtikelScreen
import com.example.healthcare.ui.screens.sistempakar.SistemPakarScreen
import com.example.healthcare.ui.screens.sistempakar.GejalaScreen
import com.example.healthcare.ui.screens.sistempakar.KondisiScreen
import com.example.healthcare.ui.screens.sistempakar.DetailScreen
import com.example.healthcare.ui.screens.sistempakar.InfoScreen
import com.example.healthcare.ui.screens.artikel.ReadArtikelScreen
import com.example.healthcare.ui.profile.ProfileScreen
import com.example.healthcare.ui.screens.ForgotPassword.ForgotPasswordScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN_SCREEN // Menggunakan konstanta AppRoutes
    ) {
        // ===================================
        // == Rute Otentikasi ==
        // ===================================
        composable(route = AppRoutes.LOGIN_SCREEN) { // Menggunakan konstanta AppRoutes
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME_SCREEN) { // Menggunakan konstanta AppRoutes
                        popUpTo(AppRoutes.LOGIN_SCREEN) { // Menggunakan konstanta AppRoutes
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppRoutes.REGISTER_SCREEN) // Menggunakan konstanta AppRoutes
                },
                onForgotPasswordClick = {
                    navController.navigate(AppRoutes.FORGOT_PASSWORD_SCREEN) // Menggunakan konstanta AppRoutes
                }
            )
        }


        composable(route = AppRoutes.REGISTER_SCREEN) { // Menggunakan konstanta AppRoutes
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // == Forgot Password ==
        composable(route = AppRoutes.FORGOT_PASSWORD_SCREEN) { // Menggunakan konstanta AppRoutes
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // ===================================
        // == Rute Utama ==
        // ===================================

        composable(route = AppRoutes.HOME_SCREEN) { // Menggunakan konstanta AppRoutes
            Scaffold(
                bottomBar = { FloatingBottomNavBar(navController) }
            ) { paddingValues ->
                HomeScreen(
                    modifier = Modifier.padding(paddingValues),
                    onSistemPakarClick = {
                        navController.navigate(AppRoutes.SISTEM_PAKAR_SCREEN) // Menggunakan konstanta AppRoutes
                    },
                    onArtikelClick = {
                        navController.navigate(AppRoutes.ARTIKEL_SCREEN) // Menggunakan konstanta AppRoutes
                    }
                )
            }
        }

        composable(route = AppRoutes.PROFILE_SCREEN) { // Menggunakan konstanta AppRoutes
            Scaffold(
                bottomBar = { FloatingBottomNavBar(navController) }
            ) { paddingValues ->
                ProfileScreen(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController,
                    onLogoutClick = {
                        navController.navigate(AppRoutes.LOGIN_SCREEN) { // Menggunakan konstanta AppRoutes
                            popUpTo(AppRoutes.HOME_SCREEN) { // Menggunakan konstanta AppRoutes
                                inclusive = true
                            }
                        }

                    }
                )
            }
        }

        composable(route = AppRoutes.ARTIKEL_SCREEN) { // Menggunakan konstanta AppRoutes
            Scaffold(
                bottomBar = { FloatingBottomNavBar(navController) }
            ) { paddingValues ->
                ArtikelScreen(
                    modifier = Modifier.padding(paddingValues),
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onArtikelDetailClick = { artikelId ->
                        // Menggunakan AppRoutes.ARTIKEL_DETAIL_ROUTE untuk navigasi
                        navController.navigate("${AppRoutes.ARTIKEL_DETAIL_ROUTE}/$artikelId")
                    }
                )
            }
        }

        composable(
            route = AppRoutes.ARTIKEL_DETAIL_SCREEN, // Menggunakan konstanta AppRoutes (yang sudah menyertakan argumen)
            arguments = listOf(navArgument(AppRoutes.ARTIKEL_DETAIL_ARG_ID) { // Menggunakan konstanta AppRoutes
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val artikelId = backStackEntry.arguments?.getString(AppRoutes.ARTIKEL_DETAIL_ARG_ID) // Menggunakan konstanta AppRoutes

            ReadArtikelScreen(
                itemId = artikelId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // ===================================
        // == Rute Sistem Pakar ==
        // ===================================

        composable(route = AppRoutes.SISTEM_PAKAR_SCREEN) { // Menggunakan konstanta AppRoutes
            Scaffold(
                bottomBar = { FloatingBottomNavBar(navController) }
            ) { paddingValues ->
                SistemPakarScreen(
                    modifier = Modifier.padding(paddingValues),
                    onMulaiClick = {
                        navController.navigate(AppRoutes.INFO_SCREEN) // Menggunakan konstanta AppRoutes
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(route = AppRoutes.INFO_SCREEN) { // Menggunakan konstanta AppRoutes
            InfoScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLanjutClick = {
                    navController.navigate(AppRoutes.GEJALA_SCREEN) // Menggunakan konstanta AppRoutes
                }
            )
        }

        composable(route = AppRoutes.GEJALA_SCREEN) { // Menggunakan konstanta AppRoutes
            GejalaScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLanjutClick = {
                    navController.navigate(AppRoutes.KONDISI_SCREEN) // Menggunakan konstanta AppRoutes
                }
            )
        }

        composable(route = AppRoutes.KONDISI_SCREEN) { // Menggunakan konstanta AppRoutes
            KondisiScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLanjutClick = {
                    val hasilId = "HasilDiagnosa_001"
                    // Menggunakan AppRoutes.PAKAR_DETAIL_ROUTE untuk navigasi
                    navController.navigate("${AppRoutes.PAKAR_DETAIL_ROUTE}/$hasilId")
                }
            )
        }

        composable(
            route = AppRoutes.PAKAR_DETAIL_SCREEN, // Menggunakan konstanta AppRoutes (yang sudah menyertakan argumen)
            arguments = listOf(navArgument(AppRoutes.PAKAR_DETAIL_ARG_ID) { // Menggunakan konstanta AppRoutes
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val pakarId = backStackEntry.arguments?.getString(AppRoutes.PAKAR_DETAIL_ARG_ID) // Menggunakan konstanta AppRoutes

            DetailScreen(
                itemId = pakarId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSelesaiClick = {
                    navController.popBackStack(
                        route = AppRoutes.SISTEM_PAKAR_SCREEN, // Menggunakan konstanta AppRoutes
                        inclusive = false
                    )
                }
            )
        }

    }
}