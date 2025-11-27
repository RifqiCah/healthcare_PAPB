package com.example.healthcare.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LoginRequiredDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Login Diperlukan") },
        text = { Text(text = "Fitur ini hanya tersedia untuk pengguna yang sudah login. Silakan login terlebih dahulu.") },
        confirmButton = {
            TextButton(onClick = onConfirmClick) {
                Text("Login Sekarang")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Batal")
            }
        }
    )
}