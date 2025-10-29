// Di dalam file /ui/screens/register/RegisterScreen.kt
package com.example.healthcare.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.ui.theme.HealthcareTheme

// Hapus import NavController, sudah tidak perlu
// import androidx.navigation.NavController

@Composable
fun RegisterScreen(
    // UBAH INI: Ganti NavController dengan lambda
    onRegisterSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE9EDF5)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registrasi",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Sudah memiliki akun?")
                    Spacer(modifier = Modifier.width(4.dp))

                    // UBAH INI: Panggil lambda onBackClick
                    TextButton(onClick = onBackClick) {
                        Text("Login Sekarang", color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Tombol Google (placeholder)
                Button(
                    onClick = { /* TODO: Google Sign-Up */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Text("Lanjutkan dengan Google", color = Color.Black)
                }

                Divider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color.Gray,
                    thickness = 1.dp
                )

                // Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Konfirmasi Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        // TODO: Validasi data registrasi di sini

                        // UBAH INI: Panggil lambda onRegisterSuccess
                        onRegisterSuccess()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Daftar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Dengan mendaftar, kamu menyetujui syarat dan ketentuan.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    HealthcareTheme {
        RegisterScreen(
            onRegisterSuccess = {
                // Aksi ketika register berhasil (dummy untuk preview)
            },
            onBackClick = {
                // Aksi ketika klik back/login (dummy untuk preview)
            }
        )
    }
}