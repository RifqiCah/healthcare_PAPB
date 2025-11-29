package com.example.healthcare.ui.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthcare.R
import com.example.healthcare.viewmodel.AuthViewModel
import com.example.healthcare.viewmodel.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import com.example.healthcare.ui.screens.login.rememberGoogleSignInLauncher // Import launcher


@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    // === State input user ===
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val authState = viewModel.authState.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val snackbarHostState = remember { SnackbarHostState() }

    // HOOK GOOGLE SIGN-IN (Navigasi ke Login Screen setelah sukses)
    val onGoogleClick = rememberGoogleSignInLauncher(
        viewModel = viewModel,
        onSignInSuccess = onBackClick // Pindah ke Login setelah Register Google sukses
    )


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // LOGO
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_removebg_preview),
                            contentDescription = "Healthcare Logo",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Text(
                    text = "HEALTH CARE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "Bergabunglah Bersama Kami!",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                // === CARD FORM ===
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
                            text = "Daftar Akun",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF418ACE),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 24.dp)
                        ) {
                            Text(
                                text = "Sudah memiliki akun?",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                            TextButton(onClick = onBackClick) {
                                Text(
                                    "Login Sekarang",
                                    color = Color(0xFF418ACE),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // TOMBOL GOOGLE FULL-WIDTH
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
                                    text = "Daftar dengan Google",
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

                        // USERNAME
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF418ACE)
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        // EMAIL
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Email,
                                    contentDescription = null,
                                    tint = Color(0xFF418ACE)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        // PASSWORD
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF418ACE)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(Modifier.height(16.dp))

                        // CONFIRM PASSWORD
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Konfirmasi Password") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF418ACE)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        imageVector = if (confirmPasswordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            isError = confirmPassword.isNotEmpty() && confirmPassword != password,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        if (confirmPassword.isNotEmpty() && confirmPassword != password) {
                            Text(
                                text = "Password tidak cocok",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(start = 8.dp, top = 4.dp)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // TERMS
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = agreeToTerms,
                                onCheckedChange = { agreeToTerms = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF418ACE)
                                )
                            )
                            Text(
                                text = "Saya menyetujui syarat dan ketentuan",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        // REGISTER BUTTON
                        Button(
                            onClick = {
                                if (password != confirmPassword) return@Button
                                viewModel.register(email, password)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = authState.value != AuthState.Loading
                                    && email.isNotEmpty()
                                    && username.isNotEmpty()
                                    && password.isNotEmpty()
                                    && confirmPassword == password
                                    && agreeToTerms,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF418ACE)
                            )
                        ) {
                            Text(
                                text = "Daftar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(16.dp))

                        // === AUTH STATE ===
                        when (val state = authState.value) {

                            is AuthState.Loading -> {
                                CircularProgressIndicator()
                            }

                            is AuthState.Error -> {
                                Text(
                                    text = state.message,
                                    color = Color.Red,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            is AuthState.Success -> {
                                // Tulis data user ke Firestore dan navigasi
                                LaunchedEffect(key1 = "register_success") {
                                    val uid = auth.currentUser?.uid
                                    if (uid != null) {
                                        Firebase.firestore.collection("users")
                                            .document(uid)
                                            .set(
                                                mapOf(
                                                    "uid" to uid,
                                                    "email" to email,
                                                    "username" to username
                                                )
                                            )
                                    }

                                    snackbarHostState.showSnackbar(
                                        message = "Pendaftaran Berhasil! Silakan Masuk.",
                                        duration = SnackbarDuration.Short
                                    )

                                    delay(800)
                                    onRegisterSuccess() // Navigasi ke Login Screen
                                }
                            }

                            else -> {}
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "© 2025 Healthcare. All rights reserved.",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}