// Di dalam file /ui/screens/home/HomeScreen.kt
package com.example.healthcare.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.healthcare.R


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    // UBAH INI: Tambahkan parameter lambda untuk navigasi
    onSistemPakarClick: () -> Unit,
    onArtikelClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(240.dp)
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 220.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HEALTH CARE",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE91E63)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SELAMAT DATANG",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = "DI HEALTHCARE",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { /*TODO: Navigasi ke 'Tentang Kita'*/ }) {
                        Text("Tentang Kita")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Layanan Kami",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2196F3),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // UBAH INI: Tambahkan onClick ke pemanggilan ServiceCard
            ServiceCard(
                title = "Sistem Pakar",
                subtitle = "Diagnosa Penyakit",
                onClick = onSistemPakarClick // Teruskan perintah navigasi
            )
            ServiceCard(
                title = "Blog Kesehatan",
                subtitle = "Artikel Kesehatan",
                onClick = onArtikelClick // Teruskan perintah navigasi
            )
            ServiceCard(
                title = "Hasil Analisa",
                subtitle = "Jurnal Sistem Pakar",
                onClick = { /* TODO: Tentukan navigasi untuk 'Hasil Analisa' */ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Perlu ini untuk Card onClick
@Composable
fun ServiceCard(
    title: String,
    subtitle: String,
    // UBAH INI: Tambahkan parameter onClick
    onClick: () -> Unit
) {
    Card(
        // UBAH INI: Tambahkan onClick ke Card
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
            Text(text = subtitle, color = Color.Gray)
        }
    }
}