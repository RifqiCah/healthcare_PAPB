// Di file /ui/screens/sistempakar/DetailScreen.kt
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

// Asumsi HeaderSection, HeroSection, dan StepperSection ada di file lain

@Composable
fun DetailScreen(
    // UBAH INI: Sesuaikan parameter
    itemId: String?,
    onSelesaiClick: () -> Unit,
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
        // StepperSection(activeStep = 4)
        // Spacer(Modifier.height(32.dp))

        // Gunakan itemId untuk menampilkan data
        Text(
            "Hasil Diagnosa untuk ID: ${itemId ?: "Tidak Ditemukan"}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(32.dp))

        Text("Artikel terkait:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(16.dp))

        repeat(2) { index ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Artikel ${index + 1}", fontWeight = FontWeight.Bold)
                    Text("Deskripsi singkat artikel ${index + 1}", fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // UBAH INI: Gunakan onBackClick
            OutlinedButton(onClick = onBackClick, shape = CircleShape) { Text("Kembali") }

            // UBAH INI: Gunakan onSelesaiClick
            Button(
                onClick = onSelesaiClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) { Text("Selesai", color = Color.White) }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDetailScreen() {
    // UBAH INI: Sesuaikan preview
    DetailScreen(
        itemId = "HasilDiagnosa_001",
        onSelesaiClick = {},
        onBackClick = {}
    )
}