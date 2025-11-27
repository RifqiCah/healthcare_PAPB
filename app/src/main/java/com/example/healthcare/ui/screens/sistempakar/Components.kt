package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R

@Composable
fun HeroSection() {
    val infiniteTransition = rememberInfiniteTransition(label = "hero")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    ) {
        // 1. GAMBAR BACKGROUND (Memenuhi Box)
        Image(
            painter = painterResource(id = R.drawable.foto_fitur_sistem_pakar),
            contentDescription = "Hero Sistem Pakar",
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentScale = ContentScale.Crop
        )

        // 2. GRADIENT OVERLAY (Agar text tetap terbaca)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f), // Gelap di atas (Status Bar area)
                            Color.Black.copy(alpha = 0.2f), // Tengah agak terang
                            Color.Black.copy(alpha = 0.8f)  // Bawah gelap lagi
                        )
                    )
                )
        )

        // 3. TEKS KONTEN
        // Menggunakan statusBarsPadding() agar teks turun otomatis di bawah jam/baterai
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo/Icon Box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🩺",
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sistem Pakar Kesehatan",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Diagnosis cerdas berdasarkan gejala yang Anda alami",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
fun StepperSection(activeStep: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepCircle(1, "Gejala", activeStep >= 1, activeStep == 1)

            // Connector Line
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(2.dp)
                    .background(
                        if (activeStep >= 2) Color(0xFF00BCD4) else Color(0xFFE0E0E0)
                    )
            )

            StepCircle(2, "Kondisi", activeStep >= 2, activeStep == 2)

            // Connector Line
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(2.dp)
                    .background(
                        if (activeStep >= 3) Color(0xFF00BCD4) else Color(0xFFE0E0E0)
                    )
            )

            StepCircle(3, "Detail", activeStep >= 3, activeStep == 3)
        }
    }
}

@Composable
fun StepCircle(number: Int, label: String, isCompleted: Boolean, isActive: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = when {
                        isActive -> Color(0xFF00BCD4)
                        isCompleted -> Color(0xFF00BCD4)
                        else -> Color(0xFFE0E0E0)
                    },
                    shape = CircleShape
                )
                .then(
                    if (isActive) {
                        Modifier.background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF00BCD4),
                                    Color(0xFF0097A7)
                                )
                            ),
                            shape = CircleShape
                        )
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                number.toString(),
                color = if (isCompleted || isActive) Color.White else Color(0xFF9E9E9E),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            label,
            fontSize = 13.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isActive) Color(0xFF00BCD4) else Color(0xFF666666)
        )
    }
}