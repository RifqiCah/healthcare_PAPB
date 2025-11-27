package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.animation.animateContentSize // Import untuk animasi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Import untuk klik
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow // Import untuk titik-titik (...)
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Hapus import hiltViewModel karena ViewModel dioper dari parameter
import com.example.healthcare.domain.model.KemungkinanPenyakit
import com.example.healthcare.viewmodel.SistemPakarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KondisiScreen(
    onLanjutClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SistemPakarViewModel // Parameter ViewModel (Shared)
) {
    val uiState by viewModel.uiState.collectAsState()
    val hasilDiagnosa = uiState.hasilDiagnosa

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hasil Diagnosa") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Komponen Header dll (Uncomment jika sudah ada filenya)
             HeaderSection()
             Spacer(Modifier.height(16.dp))
             HeroSection()
             Spacer(Modifier.height(24.dp))

             StepperSection(activeStep = 2)
             Spacer(Modifier.height(32.dp))

            Text(
                text = "Analisa AI:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Berikut adalah kemungkinan kondisi berdasarkan gejala yang Anda input:",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(16.dp))

            // --- LOGIC MENAMPILKAN HASIL ---
            if (hasilDiagnosa != null && hasilDiagnosa.kemungkinan.isNotEmpty()) {
                hasilDiagnosa.kemungkinan.forEachIndexed { index, penyakit ->
                    // Memanggil Card yang sudah di-update
                    PenyakitCard(penyakit = penyakit, rank = index + 1)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Belum ada hasil diagnosa.", color = Color.Gray)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- TOMBOL NAVIGASI BAWAH ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ubah Gejala")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onLanjutClick,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    modifier = Modifier.weight(1f),
                    enabled = hasilDiagnosa != null
                ) {
                    Text("Lihat Detail", color = Color.White)
                }
            }
        }
    }
}

// --- CARD EXPANDABLE (UPDATE TERBARU) ---
@Composable
fun PenyakitCard(penyakit: KemungkinanPenyakit, rank: Int) {
    // State untuk menyimpan status apakah kartu sedang dibuka atau ditutup
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // Animasi otomatis saat ukuran berubah
            .clickable { isExpanded = !isExpanded }, // Aksi Klik untuk toggle
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Bagian Judul dan Persentase (TETAP SAMA)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${rank}. ${penyakit.nama}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${penyakit.persentase}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (rank == 1) Color(0xFF4CAF50) else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar (TETAP SAMA)
            val animatedProgress by animateFloatAsState(
                targetValue = penyakit.persentase / 100f,
                label = "progress"
            )

            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (rank == 1) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- BAGIAN DESKRIPSI (YANG DIMODIFIKASI) ---
            Column {
                Text(
                    text = penyakit.deskripsi,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp,
                    // Logika: Jika expanded = tampil semua, jika tidak = max 2 baris
                    maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Indikator kecil untuk memberi tahu user bisa diklik
                if (!isExpanded) {
                    Text(
                        text = "Ketuk untuk baca selengkapnya...",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}