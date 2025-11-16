package com.example.healthcare.ui.screens.ForgotPassword


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        onBackClick = {},
        onResetSuccess = {}
    )
}

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onResetSuccess: () -> Unit = {}
)

{
    var email by remember { mutableStateOf("") }
    var isEmailSent by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Icon Section
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

            // Title Text
            Text(
                text = "LUPA PASSWORD?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Text(
                text = if (isEmailSent)
                    "Cek Email Anda!"
                else
                    "Jangan Khawatir!",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // Main Card
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
                    if (!isEmailSent) {
                        // Before Email Sent
                        Text(
                            text = "Reset Password",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF418ACE),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Masukkan email Anda dan kami akan mengirimkan link untuk reset password",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        // Email Field
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
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF418ACE),
                                unfocusedBorderColor = Color.LightGray
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Send Button
                        Button(
                            onClick = {
                                // TODO: Implement password reset logic
                                if (email.isNotEmpty()) {
                                    isEmailSent = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF418ACE)
                            ),
                            elevation = ButtonDefaults.buttonElevation(4.dp),
                            enabled = email.isNotEmpty()
                        ) {
                            Text(
                                text = "Kirim Link Reset",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Back to Login
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ingat password Anda?",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            TextButton(
                                onClick = onBackClick,
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    text = "Login",
                                    color = Color(0xFF418ACE),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        // After Email Sent - Success State
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Sent",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier
                                .size(80.dp)
                                .padding(bottom = 16.dp)
                        )

                        Text(
                            text = "Email Terkirim!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF418ACE),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Kami telah mengirimkan link reset password ke:",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = email,
                            fontSize = 15.sp,
                            color = Color(0xFF418ACE),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE3F2FD)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "📧 Langkah Selanjutnya:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF418ACE),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Text(
                                    text = "1. Buka email Anda\n2. Klik link reset password\n3. Buat password baru\n4. Login dengan password baru",
                                    fontSize = 13.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        // Resend Button
                        OutlinedButton(
                            onClick = {
                                // TODO: Resend email logic
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Kirim Ulang Email",
                                color = Color(0xFF418ACE),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Back to Login Button
                        Button(
                            onClick = onResetSuccess,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF418ACE)
                            ),
                            elevation = ButtonDefaults.buttonElevation(4.dp)
                        ) {
                            Text(
                                text = "Kembali ke Login",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer
            Text(
                text = "© 2025 Healthcare. All rights reserved.",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}