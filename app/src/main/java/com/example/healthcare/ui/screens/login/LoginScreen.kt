package com.example.healthcare.ui.screens.login

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

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    val authState = viewModel.authState.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2E6BA8),
                        Color(0xFF418ACE),
                        Color(0xFFE3F2FD)
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
                    Icon(
                        Icons.Default.Email,
                        contentDescription = "Logo",
                        tint = Color(0xFF418ACE),
                        modifier = Modifier.size(50.dp)
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
                        color = Color(0xFF418ACE),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.padding(bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Belum memiliki akun?")
                        TextButton(onClick = onRegisterClick) {
                            Text(
                                text = "Daftar Sekarang",
                                color = Color(0xFF418ACE),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // EMAIL FIELD
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF418ACE))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(Modifier.height(16.dp))

                    // PASSWORD FIELD
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF418ACE))
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
                        shape = RoundedCornerShape(12.dp)
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
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF418ACE))
                            )
                            Text("Ingat Saya", fontSize = 14.sp, color = Color.DarkGray)
                        }

                        TextButton(onClick = { /* TODO forgot password */ }) {
                            Text(
                                text = "Lupa Password?",
                                color = Color(0xFF418ACE),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // LOGIN BUTTON
                    Button(
                        onClick = { viewModel.login(email, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF418ACE))
                    ) {
                        Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(Modifier.height(16.dp))

                    // AUTH STATE
                    when (val state = authState.value) {
                        is AuthState.Loading -> CircularProgressIndicator()
                        is AuthState.Error -> Text(state.message, color = Color.Red)
                        is AuthState.Success -> {
                            LaunchedEffect(Unit) { onLoginSuccess() }
                        }
                        else -> {}
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "© 2025 Healthcare. All rights reserved.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}
