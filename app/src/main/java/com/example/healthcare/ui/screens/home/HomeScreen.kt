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

    // --- STATE UNTUK DIALOG TENTANG KITA ---
    var showAboutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    // --- STRUKTUR UTAMA ---
    Box(
        modifier = modifier
            .fillMaxSize()

    ) {
        // LAYER 1: BACKGROUND HEADER
        AnimatedHeroSection(
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // LAYER 2: KONTEN UTAMA
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 270.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)).background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer, // Atas
                            MaterialTheme.colorScheme.primaryContainer, // Tengah
                            MaterialTheme.colorScheme.surface           // Bawah
                        )
                    )
                ),
            color = Color.Transparent,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Welcome Card (Kita kirim aksi klik ke sini)
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 100)) +
                            slideInVertically(
                                animationSpec = tween(600, delayMillis = 100),
                                initialOffsetY = { it / 3 }
                            )
                ) {
                    WelcomeCard(
                        // ✅ SAAT DIKLIK, TAMPILKAN DIALOG
                        onAboutClick = { showAboutDialog = true }
                    )
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
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
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

                // Service Cards Logic
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
                    // TODO: Bagian Hasil Analisa nanti kita kerjakan setelah ini
                    ServiceData(
                        "Hasil Analisa",
                        "Lihat Riwayat Analisa Anda",
                        Icons.Default.Assessment,
                        { /* Logika History Nanti */ }
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
                            iconColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // --- LAYER 3: POP UP DIALOG (JIKA showAboutDialog == true) ---
        if (showAboutDialog) {
            AboutAppDialog(
                onDismiss = { showAboutDialog = false }
            )
        }
    }
}

// --- COMPONENT BARU: DIALOG TENTANG KITA ---
@Composable
fun AboutAppDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(Icons.Default.LocalHospital, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        title = {
            Text(text = "Tentang Healthcare", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    text = "Aplikasi Healthcare Sistem Pakar v1.0",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aplikasi ini dirancang untuk membantu Anda melakukan diagnosa awal penyakit berdasarkan gejala yang dirasakan menggunakan metode Sistem Pakar.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fitur Utama:\n• Diagnosa Penyakit\n• Artikel Kesehatan\n• Riwayat Pemeriksaan",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Tutup")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp)
    )
}

// --- DATA CLASS ---
data class ServiceData(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

// --- COMPONENTS LAINNYA ---

@Composable
fun AnimatedHeroSection(modifier: Modifier = Modifier) {
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
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Healthcare Background",
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .statusBarsPadding(),
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
fun WelcomeCard(
    onAboutClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp), // Tambah sedikit margin vertikal
        shape = RoundedCornerShape(24.dp), // Sudut lebih bulat
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // Tambah elevasi agar "pop up"
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // Transparent agar gradient di bawahnya terlihat
        )
    ) {
        // BOX UTAMA UNTUK LAYER BACKGROUND & KONTEN
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // BACKGROUND GRADIENT (Kombinasi Cyan ke Biru Muda)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,       // Cyan (Kiri Atas)
                            MaterialTheme.colorScheme.secondaryContainer, // Biru Muda (Kanan Bawah)
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
        ) {
            // LAYER 1: BACKGROUND PATTERN / DEKORASI (Opsional, menggunakan Ikon besar transparan)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp), // Sesuaikan tinggi
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.MedicalServices, // Ikon latar belakang
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.4f), // Sangat transparan
                    modifier = Modifier
                        .size(160.dp)
                        .offset(x = 40.dp, y = (-20).dp) // Geser sedikit keluar
                        .scale(1.2f)
                )
            }

            // LAYER 2: KONTEN TEKS & TOMBOL
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Rata kiri agar lebih dinamis dengan background
            ) {

                Text(
                    text = "Health Care",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold, // Lebih tebal
                        letterSpacing = 2.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Teman setia jaga kesehatanmu setiap hari.",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 1f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // TOMBOL TENTANG KITA (Lebih Menonjol)
                Button(
                    onClick = onAboutClick,
                    shape = RoundedCornerShape(50), // Bentuk kapsul
                    modifier = Modifier.height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface, // Tombol putih
                        contentColor = MaterialTheme.colorScheme.primary // Teks Cyan
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Tentang Kita",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(14.dp),
                color = iconColor.copy(alpha = 0.1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

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