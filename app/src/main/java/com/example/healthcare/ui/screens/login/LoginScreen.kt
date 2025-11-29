package com.example.healthcare.ui.screens.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthcare.viewmodel.AuthState
import com.example.healthcare.viewmodel.AuthViewModel
import androidx.compose.ui.res.painterResource
import com.example.healthcare.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

// === FUNGSI GOOGLE SIGN-IN LAUNCHER (Digunakan di Login & Register) ===
@Composable
fun rememberGoogleSignInLauncher(
    viewModel: AuthViewModel,
    onSignInSuccess: () -> Unit
): () -> Unit {
    val context = LocalContext.current

    // Konfigurasi Google Sign-In Options (GSO)
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val idToken = account.idToken

            if (idToken != null) {
                // Kirim token ke ViewModel untuk diotentikasi Firebase
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                viewModel.signInWithGoogle(credential)
            } else {
                Log.e("GoogleSignIn", "ID Token is null.")
                viewModel.setAuthState(AuthState.Error("Gagal mendapatkan token Google."))
            }
        } catch (e: ApiException) {
            // Penanganan error jika dibatalkan atau gagal
            if (e.statusCode != 12501) { // 12501 = Cancelled by user
                Log.w("GoogleSignIn", "Google sign in failed", e)
                viewModel.setAuthState(AuthState.Error("Sign-In Google dibatalkan atau gagal: ${e.statusCode}"))
            } else {
                viewModel.setAuthState(AuthState.Idle)
            }
        }
    }

    // Mengamati AuthState untuk navigasi
    val authState = viewModel.authState.collectAsState()
    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Success) {
            onSignInSuccess()
        }
    }

    // Mengembalikan fungsi yang memicu Google Sign-In UI
    return { launcher.launch(googleSignInClient.signInIntent) }
}
// ====================================================================


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGuestClick: () -> Unit, // Parameter Tamu
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val authState = viewModel.authState.collectAsState()
    val scrollState = rememberScrollState()

    val onGoogleClick = rememberGoogleSignInLauncher(
        viewModel = viewModel,
        onSignInSuccess = onLoginSuccess
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF5B7BA4),
                        Color(0xFF6B8BB4),
                        Color(0xFFD8E0EB)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // LOGO AREA
            Card(
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(50.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_removebg_preview),
                        contentDescription = "Healthcare Logo",
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            // TITLE
            Text(
                text = "HEALTH CARE",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Text(
                text = "Selamat Datang Kembali!",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // LOGIN CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Masuk",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5B7BA4),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.padding(bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Belum memiliki akun?",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        TextButton(
                            onClick = onRegisterClick,
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = "Daftar Sekarang",
                                color = Color(0xFF5B7BA4),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // TOMBOL GOOGLE
                    OutlinedButton(
                        onClick = onGoogleClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_google_icon),
                                contentDescription = "Google Logo",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Masuk dengan Google",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }
                    }

                    // Divider "atau"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f))
                        Text(
                            text = "atau",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                        Divider(modifier = Modifier.weight(1f))
                    }

                    // EMAIL FIELD
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Username atau Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF5B7BA4))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5B7BA4),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(Modifier.height(16.dp))

                    // PASSWORD FIELD
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF5B7BA4))
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF5B7BA4),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    Spacer(Modifier.height(12.dp))

                    // REMEMBER ME
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF5B7BA4))
                            )
                            Text("Ingat Saya", fontSize = 14.sp, color = Color.DarkGray)
                        }
                        TextButton(onClick = onForgotPasswordClick) {
                            Text(
                                text = "Lupa Password?",
                                color = Color(0xFF5B7BA4),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // LOGIN BUTTON
                    Button(
                        onClick = {
                            if (email.isNotEmpty() && password.isNotEmpty()) {
                                viewModel.login(email, password)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B7BA4)),
                        elevation = ButtonDefaults.buttonElevation(4.dp),
                        enabled = authState.value != AuthState.Loading
                    ) {
                        Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    // TOMBOL MASUK SEBAGAI TAMU (DENGAN LOGIC SIGNOUT)
                    Spacer(Modifier.height(12.dp))
                    TextButton(
                        onClick = {
                            // WAJIB: Bersihkan sesi user yang sudah ada
                            FirebaseAuth.getInstance().signOut()
                            // Lanjutkan navigasi ke HomeScreen (Tamu)
                            onGuestClick()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Masuk sebagai Tamu",
                            color = Color(0xFF5B7BA4),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    // AUTH STATE
                    when (val state = authState.value) {
                        is AuthState.Loading -> CircularProgressIndicator()
                        is AuthState.Error -> Text(
                            text = state.message,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        is AuthState.Success -> {
                            // Navigasi dihandle LaunchedEffect
                        }
                        else -> {}
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "© 2025 Healthcare. All rights reserved.",
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}