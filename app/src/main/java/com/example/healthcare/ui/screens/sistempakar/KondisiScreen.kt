package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.data.model.DiagnosaResult
import com.example.healthcare.viewmodel.SistemPakarViewModel

@Composable
fun KondisiScreen(
    onLanjutClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SistemPakarViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val hasilDiagnosa = uiState.hasilDiagnosa

    // --- STRUKTUR UTAMA (LAYER STACKING) ---
    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        // ==========================================================
        // LAYER 1: BACKGROUND HEADER (GAMBAR)
        // ==========================================================
        HeroSection(
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // ==========================================================
        // LAYER 2: KONTEN UTAMA (LEMBARAN MELENGKUNG)
        // ==========================================================
        Surface(
            modifier = Modifier
                .fillMaxSize()
                // PENTING: Turunkan 270dp agar gambar Hero (300dp) terlihat
                .padding(top = 270.dp)
                // PENTING: Lengkungan sudut atas
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Spacer Header (Pengganti HeroSection di dalam list)
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 2. STEPPER SECTION
                item {
                    StepperSection(activeStep = 2)
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 3. HEADER TEXT
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Analisa AI:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary // Cyan
                        )
                        Text(
                            text = "Berikut adalah beberapa kemungkinan kondisi berdasarkan gejala yang Anda masukkan:",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant // Abu-abu teks
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 4. HASIL DIAGNOSA
                if (!hasilDiagnosa.isNullOrEmpty()) {
                    itemsIndexed(hasilDiagnosa) { index, penyakit ->
                        PenyakitCard(
                            penyakit = penyakit,
                            rank = index + 1,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
                    }
                } else {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            } else {
                                Text(
                                    text = "Belum ada hasil diagnosa.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // 5. TOMBOL NAVIGASI
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onBackClick,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Ubah Gejala")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = onLanjutClick,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.weight(1f),
                            enabled = !hasilDiagnosa.isNullOrEmpty()
                        ) {
                            Text("Lihat Detail")
                        }
                    }
                    // Spacer bawah agar scroll bisa mentok sampai tombol terlihat jelas
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

// --- CARD EXPANDABLE ---
@Composable
fun PenyakitCard(
    penyakit: DiagnosaResult,
    rank: Int,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Logika Warna: Rank 1 (Top) Hijau, Sisanya Ikut Tema (Cyan)
    val highlightColor = if (rank == 1) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary

    // Warna teks badge
    val badgeTextColor = Color.White

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Bagian Judul dan Persentase
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Badge Rank
                    Surface(
                        shape = CircleShape,
                        color = highlightColor,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = rank.toString(),
                                color = badgeTextColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = penyakit.penyakit,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = "${penyakit.persentase.toInt()}%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = highlightColor
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            val animatedProgress by animateFloatAsState(
                targetValue = penyakit.persentase / 100f,
                label = "progress"
            )

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = highlightColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Deskripsi
            Column {
                Text(
                    text = penyakit.deskripsi,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (!isExpanded) {
                    Text(
                        text = "Ketuk untuk baca selengkapnya...",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}