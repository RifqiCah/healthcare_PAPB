// Di file /ui/screens/sistempakar/KondisiScreen.kt
package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Import Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Asumsi HeaderSection, HeroSection, dan StepperSection
// ada di file lain dan di-import di sini.

@Composable
fun KondisiScreen(
    // UBAH INI: Ganti nama parameter agar konsisten
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
        // StepperSection(activeStep = 3)
        // Spacer(Modifier.height(32.dp))

        Text("Kondisi yang memungkinkan:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(16.dp))

        repeat(3) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "Kondisi ${index + 1}",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // UBAH INI: Gunakan onBackClick
            OutlinedButton(onClick = onBackClick, shape = CircleShape) { Text("Kembali") }

            // UBAH INI: Gunakan onLanjutClick
            Button(
                onClick = onLanjutClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) { Text("Lanjut", color = Color.White) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewKondisiScreen() {
    // UBAH INI: Sesuaikan dengan nama parameter baru
    KondisiScreen(onLanjutClick = {}, onBackClick = {})
}