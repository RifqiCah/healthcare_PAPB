package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.healthcare.viewmodel.SistemPakarViewModel
import com.example.healthcare.domain.model.DiagnosisHistory

@Composable
fun SistemPakarScreen(
    modifier: Modifier = Modifier,
    onMulaiClick: () -> Unit,
    onBackClick: () -> Unit, // (Opsional jika ingin tombol back di header)
    viewModel: SistemPakarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val historyList = uiState.historyList

    // --- AUTO REFRESH HISTORY SAAT KEMBALI KE LAYAR INI ---
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchHistory()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // --- STRUKTUR UTAMA (LAYER STACKING - Qpon Style) ---
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
                // Turunkan 270dp agar gambar Hero (300dp) terlihat
                .padding(top = 270.dp)
                // Lengkungan sudut atas
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
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 1. Spacer agar konten tidak terlalu nempel ke lengkungan atas
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 2. KARTU "MULAI PENGECEKAN"
                item {
                    StartCheckupCard(onMulaiClick = onMulaiClick)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 3. HEADER RIWAYAT
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Riwayat Pengecekan",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 4. LIST RIWAYAT / EMPTY STATE
                if (historyList.isEmpty()) {
                    item {
                        EmptyHistoryState()
                    }
                } else {
                    // Header Tabel
                    item {
                        HistoryTableHeader()
                    }

                    // Isi Tabel
                    itemsIndexed(historyList) { index, item ->
                        HistoryRowItem(item = item, isLast = index == historyList.lastIndex)
                    }
                }
            }
        }
    }
}

// --- KOMPONEN: KARTU MULAI ---
@Composable
fun StartCheckupCard(onMulaiClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.LocalHospital,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Pengecekan Gejala Kesehatan",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(vertical = 8.dp),
                thickness = 3.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )

            Text(
                text = "Dapatkan informasi awal tentang kondisi kesehatan Anda berdasarkan gejala yang dialami.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onMulaiClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mulai Analisa",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

// --- KOMPONEN: EMPTY STATE ---
@Composable
fun EmptyHistoryState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.outlineVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Belum ada riwayat pemeriksaan",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// --- KOMPONEN: TABLE HEADER ---
@Composable
fun HistoryTableHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 8.dp), // Sedikit jarak dengan item pertama
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 4.dp, bottomEnd = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Tanggal",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1.1f),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                "Waktu",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(0.8f),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                "Hasil Diagnosa",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1.5f),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

// --- KOMPONEN: HISTORY ROW ITEM ---
@Composable
fun HistoryRowItem(
    item: com.example.healthcare.domain.model.DiagnosisHistory, // Pastikan import sesuai model kamu
    isLast: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Tanggal
            Text(
                text = item.tanggal,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1.1f),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Waktu
            Text(
                text = item.waktu,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.8f),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Hasil (Highlighted)
            Column(modifier = Modifier.weight(1.5f)) {
                Text(
                    text = item.penyakit,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${String.format("%.2f", item.persentase)}%",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}