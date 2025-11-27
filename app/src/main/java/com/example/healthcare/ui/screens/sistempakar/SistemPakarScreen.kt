package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect // Import DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner // Import Lifecycle
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle // Import Lifecycle
import androidx.lifecycle.LifecycleEventObserver // Import Lifecycle
import com.example.healthcare.R
import com.example.healthcare.viewmodel.SistemPakarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SistemPakarScreen(
    modifier: Modifier = Modifier,
    onMulaiClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SistemPakarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val historyList = uiState.historyList

    // --- 1. FITUR AUTO REFRESH (Agar data muncul saat kembali dari detail) ---
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchHistory() // Refresh data setiap layar muncul
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    // -----------------------------------------------------------------------

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // --- ITEM 1: HEADER IMAGE ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bg_pakar),
                    contentDescription = "Header",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.4f)))

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("SISTEM PAKAR", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color.White))
                    Text("DIAGNOSA PENYAKIT", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = Color.White))
                }
            }
        }

        // --- ITEM 2: INFO PENGECEKAN ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Pengecekan Gejala Kesehatan", style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
                    Text("Dapatkan informasi awal tentang kondisi kesehatan\nAnda berdasarkan gejala yang dialami", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // --- ITEM 3: CARA PENGGUNAAN ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cara Menggunakan:", style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(12.dp))
                    val steps = listOf("Pastikan Anda telah masuk ke sistem.", "Tekan tombol Mulai di bawah.", "Masukkan umur dan gejala.", "Lihat hasil diagnosa & artikel.")
                    steps.forEachIndexed { index, step ->
                        Text("${index + 1}. $step", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), modifier = Modifier.padding(vertical = 4.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onMulaiClick, modifier = Modifier.fillMaxWidth()) { Text("Mulai Pengecekan") }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        // --- ITEM 4: HEADER RIWAYAT ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Text("Riwayat Pengecekan", style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary))
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))

                    // Table Header
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // --- 2. UBAH WEIGHT AGAR LEBIH PROPORSIONAL ---
                        Text("Tanggal", style = HeaderStyle(), modifier = Modifier.weight(1.1f)) // Sedikit dikecilkan
                        Text("Waktu", style = HeaderStyle(), modifier = Modifier.weight(0.6f))   // Dikecilkan banyak (waktu cuma butuh dikit)
                        Text("Hasil", style = HeaderStyle(), modifier = Modifier.weight(2.0f))   // Dibesarkan (Paling penting)
                    }
                    HorizontalDivider(thickness = 1.dp, color = Color(0xFFE0E0E0))

                    if (historyList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Belum ada riwayat pemeriksaan.", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp, color = Color.Gray))
                        }
                    } else {
                        Column {
                            historyList.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    // Ganti ke Top agar teks panjang terlihat rapi mulai dari atas
                                    verticalAlignment = Alignment.Top
                                ) {
                                    // --- 3. TERAPKAN WEIGHT & HAPUS MAXLINES ---
                                    Text(item.tanggal, style = ItemStyle(), modifier = Modifier.weight(1.1f))
                                    Text(item.waktu, style = ItemStyle(), modifier = Modifier.weight(0.6f))

                                    Text(
                                        text = "${item.penyakit} (${item.persentase}%)",
                                        style = ItemStyle().copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.weight(2.0f),
                                        // HAPUS maxLines dan overflow agar teks turun ke bawah (wrapping)
                                    )
                                }
                                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderStyle() = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp, textAlign = TextAlign.Start)

@Composable
fun ItemStyle() = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp, textAlign = TextAlign.Start)