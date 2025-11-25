package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack // Icon Back
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthcare.domain.model.KemungkinanPenyakit

import com.example.healthcare.viewmodel.SistemPakarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KondisiScreen(
    onLanjutClick: () -> Unit,
    onBackClick: () -> Unit, // Ini fungsi kembalinya
    viewModel: SistemPakarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hasilDiagnosa = uiState.hasilDiagnosa

    // Gunakan Scaffold agar kita bisa pasang Top Bar (Tombol Back di atas)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hasil Diagnosa") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) { // Tombol Panah Kembali
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5) // Samakan dengan background
                )
            )
        }
    ) { paddingValues ->

        // Konten utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues) // Penting agar tidak ketutup TopBar
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Komponen Header dll (Uncomment jika sudah ada filenya)
             HeaderSection()
             Spacer(Modifier.height(16.dp))
             HeroSection()
             Spacer(Modifier.height(24.dp))

            // Stepper di step 2 (Hasil)
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
                // Tampilkan setiap kemungkinan penyakit
                hasilDiagnosa.kemungkinan.forEachIndexed { index, penyakit ->
                    PenyakitCard(penyakit = penyakit, rank = index + 1)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                // Fallback jika data kosong/loading
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
                // Tombol Kembali di Bawah (Saya ganti teksnya jadi "Ubah Gejala" biar lebih jelas)
                OutlinedButton(
                    onClick = onBackClick,
                    shape = CircleShape,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ubah Gejala")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onLanjutClick, // Ke Detail Screen
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

// --- CARD UNTUK MENAMPILKAN PENYAKIT ---
@Composable
fun PenyakitCard(penyakit: KemungkinanPenyakit, rank: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nama Penyakit
                Text(
                    text = "${rank}. ${penyakit.nama}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Persentase Teks
                Text(
                    text = "${penyakit.persentase}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (rank == 1) Color(0xFF4CAF50) else Color.Gray // Hijau untuk ranking 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
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

            // Deskripsi Singkat
            Text(
                text = penyakit.deskripsi,
                fontSize = 13.sp,
                color = Color.Gray,
                maxLines = 2,
                lineHeight = 18.sp
            )
        }
    }
}