package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// ✅ GANTI IMPORT INI:
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

    // Gunakan Box agar layout fleksibel
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. HERO SECTION
            item {
                HeroSection()
                Spacer(modifier = Modifier.height(24.dp))
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
                        color = Color(0xFF00BCD4)
                    )
                    Text(
                        text = "Berikut adalah beberapa kemungkinan kondisi berdasarkan gejala yang Anda masukkan:",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 4. HASIL DIAGNOSA (PERBAIKAN LOGIC)
            // ✅ Tidak perlu cek .kemungkinan, karena hasilDiagnosa itu sendiri sudah List
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
                            CircularProgressIndicator()
                        } else {
                            // Tampilkan pesan hanya jika tidak loading dan data kosong
                            Text("Belum ada hasil diagnosa.", color = Color.Gray)
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
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Ubah Gejala", color = Color(0xFF00BCD4))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = onLanjutClick,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF00BCD4)
                        ),
                        modifier = Modifier.weight(1f),
                        // ✅ Enabled jika list tidak null
                        enabled = !hasilDiagnosa.isNullOrEmpty()
                    ) {
                        Text("Lihat Detail", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

// --- CARD EXPANDABLE ---
@Composable
fun PenyakitCard(
    penyakit: DiagnosaResult, // ✅ Ganti tipe data ke DiagnosaResult
    rank: Int,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
                        color = if (rank == 1) Color(0xFF4CAF50) else Color(0xFF00BCD4),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = rank.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        // ✅ Ganti .nama menjadi .penyakit
                        text = penyakit.penyakit,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    // ✅ Format persentase (Float ke Int atau String)
                    text = "${penyakit.persentase.toInt()}%",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (rank == 1) Color(0xFF4CAF50) else Color(0xFF00BCD4)
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
                color = if (rank == 1) Color(0xFF4CAF50) else Color(0xFF00BCD4),
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Deskripsi
            Column {
                Text(
                    text = penyakit.deskripsi,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis
                )

                if (!isExpanded) {
                    Text(
                        text = "Ketuk untuk baca selengkapnya...",
                        fontSize = 13.sp,
                        color = Color(0xFF00BCD4),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}