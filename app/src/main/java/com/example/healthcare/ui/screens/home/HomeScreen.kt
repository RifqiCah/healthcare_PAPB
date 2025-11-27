package com.example.healthcare.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onSistemPakarClick: () -> Unit,
    onArtikelClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent, // Atas transparan biar gambar asli kelihatan
                        MaterialTheme.colorScheme.primaryContainer, // Tengah Cyan
                        MaterialTheme.colorScheme.surface //
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            // Hero Section
            AnimatedHeroSection()

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Welcome Card
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 100)) +
                            slideInVertically(
                                animationSpec = tween(600, delayMillis = 100),
                                initialOffsetY = { it / 3 }
                            )
                ) {
                    WelcomeCard()
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Services Header
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 200))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices, // Ganti icon biar relevan
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary, // Cyan
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Layanan Kami",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                // Service Cards
                val services = listOf(
                    ServiceData(
                        "Sistem Pakar",
                        "Diagnosa Penyakit Anda di sini",
                        Icons.Default.Psychology,
                        onSistemPakarClick
                    ),
                    ServiceData(
                        "Artikel Kesehatan",
                        "Informasi Terpercaya untuk Hidup Lebih Sehat",
                        Icons.AutoMirrored.Filled.Article,
                        onArtikelClick
                    ),
                    ServiceData(
                        "Hasil Analisa",
                        "Lihat Riwayat Analisa Anda",
                        Icons.Default.Assessment,
                        {}
                    )
                )

                services.forEachIndexed { index, service ->
                    var cardVisible by remember { mutableStateOf(false) }

                    LaunchedEffect(visible) {
                        if (visible) {
                            delay((300 + index * 100).toLong())
                            cardVisible = true
                        }
                    }

                    AnimatedVisibility(
                        visible = cardVisible,
                        enter = fadeIn(animationSpec = tween(500)) +
                                slideInVertically(
                                    animationSpec = tween(500),
                                    initialOffsetY = { it / 2 }
                                )
                    ) {
                        ModernServiceCard(
                            title = service.title,
                            subtitle = service.subtitle,
                            icon = service.icon,
                            onClick = service.onClick,
                            // Warna Ikon bervariasi tapi tetap dalam tema
                            iconColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // SPACER PENTING UNTUK FLOATING NAVBAR
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

data class ServiceData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun AnimatedHeroSection() {
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
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Healthcare Background",
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentScale = ContentScale.Crop
        )

        // Gradient Overlay (Lebih ke Biru Tua/Cyan biar teks putih kebaca)
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        colors = listOf(
//                            Color.Transparent, // Atas transparan biar gambar asli kelihatan
//                            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), // Tengah Cyan
//                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f) // Bawah Biru Tua
//                        )
//                    )
//                )
//        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        )
        {
            PulsingHealthIcon()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "HEALTH CARE",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Solusi Kesehatan Digital Terpercaya",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PulsingHealthIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .scale(scale)
            .background(
                color = Color.White.copy(alpha = 0.2f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun WelcomeCard() {
    // Card Putih Bersih di atas background abu muda
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background), //putih
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SELAMAT DATANG",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                ),
                color = MaterialTheme.colorScheme.secondary // Biru Tua
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "DI HEALTHCARE",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary // Cyan
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* TODO: Navigasi */ },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Tombol Cyan
                    contentColor = MaterialTheme.colorScheme.onPrimary // Teks Putih
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Tentang Kita",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernServiceCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    iconColor: Color
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )

    Card(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .scale(scale),
        shape = RoundedCornerShape(20.dp),
        // Gunakan Surface/Putih untuk kartu agar terlihat menonjol
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(14.dp),
                color = iconColor.copy(alpha = 0.1f) // Warna background ikon pudar
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor, // Warna ikon asli
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Arrow Icon
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}