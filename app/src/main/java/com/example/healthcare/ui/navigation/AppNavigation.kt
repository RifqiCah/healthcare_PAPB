package com.example.healthcare.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.healthcare.ui.components.StandardBottomNavBarWithLabel
import com.example.healthcare.ui.components.LoginRequiredDialog

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

    var showLoginDialog by remember { mutableStateOf(false) }

    fun checkAuthAndNavigate(action: () -> Unit) {
        if (auth.currentUser != null) {
            action()
        } else {
            showLoginDialog = true
        }
    }

    fun navigateToLogin() {
        showLoginDialog = false
        navController.navigate(AppRoutes.LOGIN_SCREEN) {
            popUpTo(AppRoutes.HOME_SCREEN) { inclusive = true }
        }
    }

    if (showLoginDialog) {
        LoginRequiredDialog(
            onDismissRequest = { showLoginDialog = false },
            onConfirmClick = { navigateToLogin() }
        )
    }

    @Composable
    fun LoginGuardScreen(content: @Composable () -> Unit) {
        if (auth.currentUser != null) {
            content()
        } else {
            LaunchedEffect(Unit) {
                showLoginDialog = true
                navController.popBackStack()
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
                onForgotPasswordClick = { navController.navigate(AppRoutes.FORGOT_PASSWORD_SCREEN) },
                onGuestClick = {
                    navController.navigate(AppRoutes.HOME_SCREEN) {
                        popUpTo(AppRoutes.LOGIN_SCREEN) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoutes.REGISTER_SCREEN) {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(AppRoutes.FORGOT_PASSWORD_SCREEN) {
            ForgotPasswordScreen(onBackClick = { navController.popBackStack() })
        }

        // == HOME SCREEN (Dengan Bottom Navbar) ==
        composable(AppRoutes.HOME_SCREEN) {
            Scaffold(
                bottomBar = {
                    StandardBottomNavBarWithLabel(navController = navController)
                }
            ) { paddingValues ->
                HomeScreen(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    onSistemPakarClick = {
                        checkAuthAndNavigate {
                            navController.navigate(AppRoutes.SISTEM_PAKAR_SCREEN)
                        }
                    },
                    onArtikelClick = {
                        checkAuthAndNavigate {
                            navController.navigate(AppRoutes.ARTIKEL_SCREEN)
                        }
                    }
                )
            }
        }

        // == PROFILE (Dengan Bottom Navbar) ==
        composable(AppRoutes.PROFILE_SCREEN) {
            LoginGuardScreen {
                Scaffold(
                    bottomBar = {
                        StandardBottomNavBarWithLabel(navController = navController)
                    }
                ) { paddingValues ->
                    ProfileScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        navController = navController,
                        onLogoutClick = {
                            auth.signOut()
                            navigateToLogin()
                        }
                    )
                }
            }
        }

        // == ARTIKEL (Dengan Bottom Navbar) ==
        composable(AppRoutes.ARTIKEL_SCREEN) {
            LoginGuardScreen {
                Scaffold(
                    bottomBar = {
                        StandardBottomNavBarWithLabel(navController = navController)
                    }
                ) { paddingValues ->
                    ArtikelScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        onBackClick = { navController.popBackStack() },
                        onArtikelDetailClick = { id ->
                            navController.navigate("${AppRoutes.ARTIKEL_DETAIL_ROUTE}/$id")
                        }
                    )
                }
            }
        }

        // == ARTIKEL DETAIL (Tanpa Bottom Navbar) ==
        composable(
            route = AppRoutes.ARTIKEL_DETAIL_SCREEN,
            arguments = listOf(
                navArgument(AppRoutes.ARTIKEL_DETAIL_ARG_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            LoginGuardScreen {
                val artikelId = backStackEntry.arguments?.getString(AppRoutes.ARTIKEL_DETAIL_ARG_ID)
                ReadArtikelScreen(
                    itemId = artikelId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        // == SISTEM PAKAR (Dengan Bottom Navbar) ==
        composable(AppRoutes.SISTEM_PAKAR_SCREEN) {
            LoginGuardScreen {
                Scaffold(
                    bottomBar = {
                        StandardBottomNavBarWithLabel(navController = navController)
                    }
                ) { paddingValues ->
                    SistemPakarScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        onMulaiClick = { navController.navigate("sistem_pakar_flow") },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        // == FLOW DIAGNOSA (Tanpa Navbar, Full Screen) ==
        navigation(
            startDestination = AppRoutes.GEJALA_SCREEN,
            route = "sistem_pakar_flow"
        ) {
            composable(AppRoutes.GEJALA_SCREEN) { backStackEntry ->
                LoginGuardScreen {
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("sistem_pakar_flow")
                    }
                    val vm = hiltViewModel<SistemPakarViewModel>(parentEntry)
                    GejalaScreen(
                        viewModel = vm,
                        onBackClick = { navController.popBackStack() },
                        onLanjutClick = { navController.navigate(AppRoutes.KONDISI_SCREEN) }
                    )
                }
            }

            composable(AppRoutes.KONDISI_SCREEN) { backStackEntry ->
                LoginGuardScreen {
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("sistem_pakar_flow")
                    }
                    val vm = hiltViewModel<SistemPakarViewModel>(parentEntry)
                    KondisiScreen(
                        viewModel = vm,
                        onBackClick = {
                            vm.clearDiagnosa()
                            navController.popBackStack()
                        },
                        onLanjutClick = {
                            navController.navigate("${AppRoutes.PAKAR_DETAIL_ROUTE}/result")
                        }
                    )
                }
            }

            composable(
                route = AppRoutes.PAKAR_DETAIL_SCREEN,
                arguments = listOf(
                    navArgument(AppRoutes.PAKAR_DETAIL_ARG_ID) {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                LoginGuardScreen {
                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry("sistem_pakar_flow")
                    }
                    val vm = hiltViewModel<SistemPakarViewModel>(parentEntry)
                    val id = backStackEntry.arguments?.getString(AppRoutes.PAKAR_DETAIL_ARG_ID)
                    DetailScreen(
                        itemId = id,
                        viewModel = vm,
                        onBackClick = { navController.popBackStack() },
                        onSelesaiClick = {
                            vm.saveResultToHistory()
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
}