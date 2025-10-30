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
import com.example.healthcare.ui.components.BottomNavBar

// --- Import semua Screen Anda di sini ---
// Pastikan path-nya sesuai dengan struktur folder Anda
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

@Composable
fun AppNavigation() {
    // 1. Membuat NavController: Ini adalah "otak" yang mengelola navigasi
    val navController = rememberNavController()

    // 2. Membuat NavHost: Ini adalah "wadah" yang akan menampilkan screen
    NavHost(
        navController = navController,
        // Tentukan layar pertama yang muncul saat aplikasi dibuka
        startDestination = AppRoutes.LOGIN_SCREEN
    ) {
        // 3. Daftarkan semua screen Anda di sini

        // == Rute Otentikasi ==

        composable(route = AppRoutes.LOGIN_SCREEN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME_SCREEN) {
                        // Hapus "login" dari tumpukan (back stack)
                        popUpTo(AppRoutes.LOGIN_SCREEN) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(AppRoutes.REGISTER_SCREEN)
                }
            )
        }

        composable(route = AppRoutes.REGISTER_SCREEN) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack() // Kembali ke login
                },
                onBackClick = {
                    navController.popBackStack() // Kembali ke layar sebelumnya
                }
            )
        }

        // == Rute Utama ==

        composable(route = AppRoutes.HOME_SCREEN) {
            Scaffold(
                bottomBar = { BottomNavBar(navController) }
            ) { paddingValues ->
                HomeScreen(
                    modifier = Modifier.padding(paddingValues),
                    onSistemPakarClick = {
                        navController.navigate(AppRoutes.SISTEM_PAKAR_SCREEN)
                    },
                    onArtikelClick = {
                        navController.navigate(AppRoutes.ARTIKEL_SCREEN)
                    }
                )
            }
        }

        composable(route = AppRoutes.ARTIKEL_SCREEN) {
            Scaffold(
                bottomBar = { BottomNavBar(navController) }
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

        // 1. Rute untuk Detail Artikel
        composable(
            route = AppRoutes.ARTIKEL_DETAIL_SCREEN, // "artikel_detail/{artikelId}"
            arguments = listOf(navArgument(AppRoutes.ARTIKEL_DETAIL_ARG_ID) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val artikelId = backStackEntry.arguments?.getString(AppRoutes.ARTIKEL_DETAIL_ARG_ID)

            // Panggil ReadArtikelScreen
            ReadArtikelScreen(
                itemId = artikelId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // == Rute Sistem Pakar ==

        composable(route = AppRoutes.SISTEM_PAKAR_SCREEN) {
            Scaffold(
                bottomBar = { BottomNavBar(navController) }
            ) { paddingValues ->
                SistemPakarScreen(
                    modifier = Modifier.padding(paddingValues),
                    onMulaiClick = {
                        navController.navigate(AppRoutes.INFO_SCREEN)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(route = AppRoutes.GEJALA_SCREEN) {
            GejalaScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLanjutClick = {
                    // "Lanjut" akan menavigasi ke KondisiScreen
                    navController.navigate(AppRoutes.KONDISI_SCREEN)
                }
            )
        }

        composable(route = AppRoutes.KONDISI_SCREEN) {
            KondisiScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLanjutClick = {
                    // "Lanjut" akan menavigasi ke layar hasil/detail
                    // Kita akan kirim ID hasil diagnosa (sebagai placeholder)
                    val hasilId = "HasilDiagnosa_001" // Nanti ini didapat dari ViewModel

                    navController.navigate("${AppRoutes.PAKAR_DETAIL_ROUTE}/$hasilId")
                }
            )
        }

        composable(route = AppRoutes.INFO_SCREEN) {
            InfoScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                // TAMBAHKAN INI: Tentukan aksi "Lanjut"
                onLanjutClick = {
                    // "Lanjut" dari InfoScreen (Step 1) pergi ke GejalaScreen (Step 2)
                    navController.navigate(AppRoutes.GEJALA_SCREEN)
                }
            )
        }

        // == Rute Detail (Dengan Argumen) ==

        composable(
            route = AppRoutes.PAKAR_DETAIL_SCREEN, // "pakar_detail/{pakarId}"
            arguments = listOf(navArgument(AppRoutes.PAKAR_DETAIL_ARG_ID) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val pakarId = backStackEntry.arguments?.getString(AppRoutes.PAKAR_DETAIL_ARG_ID)

            // UBAH INI: Sesuaikan pemanggilannya
            DetailScreen(
                itemId = pakarId,
                onBackClick = {
                    navController.popBackStack()
                },
                // TAMBAHKAN INI: Tentukan aksi untuk "Selesai"
                onSelesaiClick = {
                    // Kembali ke layar utama Sistem Pakar
                    navController.popBackStack(
                        route = AppRoutes.SISTEM_PAKAR_SCREEN,
                        inclusive = false // false = jangan tutup SistemPakarScreen
                    )

                    // Alternatif: Kembali ke Home
                    // navController.navigate(AppRoutes.HOME_SCREEN) {
                    //    popUpTo(AppRoutes.HOME_SCREEN) { inclusive = true }
                    // }
                }
            )
        }
    }
}