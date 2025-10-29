// Di file /ui/screens/sistempakar/InfoScreen.kt
package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R

// Asumsi HeaderSection, HeroSection, dan StepperSection ada di file lain

@Composable
fun InfoScreen(
    // UBAH INI: Sesuaikan parameter
    onLanjutClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Panggil fungsi-fungsi UI Anda
        // HeaderSection()
        // Spacer(Modifier.height(16.dp))
        // HeroSection()
        // Spacer(Modifier.height(24.dp))
        // StepperSection(activeStep = 1)
        // Spacer(Modifier.height(32.dp))

        // Konten Info
        Text("Umur", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Masukkan umur Anda") },
            // TODO: Buat ini 'enabled = true' dan gunakan 'remember'
            enabled = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Text("Jenis Kelamin:", fontWeight = FontWeight.SemiBold)
        Row(Modifier.padding(top = 8.dp)) {
            OutlinedButton(
                onClick = {},
                // TODO: Buat ini 'enabled = true'
                enabled = true,
                modifier = Modifier.weight(1f)
            ) {
                Text("Laki-laki")
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = {},
                // TODO: Buat ini 'enabled = true'
                enabled = true,
                modifier = Modifier.weight(1f)
            ) {
                Text("Perempuan")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // UBAH INI: Tambahkan Row untuk tombol Kembali dan Lanjut
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = onBackClick, // Gunakan onBackClick
                shape = CircleShape
            ) { Text("Kembali") }

            Button(
                onClick = onLanjutClick, // Gunakan onLanjutClick
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) { Text("Lanjut", color = Color.White) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewInfoScreen() {
    // UBAH INI: Sesuaikan preview
    InfoScreen(onLanjutClick = {}, onBackClick = {})
}