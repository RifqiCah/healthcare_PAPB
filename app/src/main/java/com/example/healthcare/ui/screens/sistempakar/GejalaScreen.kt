package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthcare.viewmodel.SistemPakarViewModel

@Composable
fun GejalaScreen(
    onBackClick: () -> Unit,
    onLanjutClick: () -> Unit,
    viewModel: SistemPakarViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // --- TRIGGER NAVIGASI ---
    LaunchedEffect(uiState.hasilDiagnosa) {
        if (!uiState.hasilDiagnosa.isNullOrEmpty()) {
            onLanjutClick()
        }
    }

    val serverSymptoms = uiState.availableSymptoms

    val displayedGejala = remember(searchQuery, serverSymptoms, uiState.selectedGejala) {
        if (searchQuery.isBlank()) {
            val top5 = serverSymptoms.take(5)
            val selectedButHidden = serverSymptoms.filter { uiState.selectedGejala.contains(it.id) }
            (top5 + selectedButHidden).distinctBy { it.id }
        } else {
            serverSymptoms.filter {
                it.label.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    // --- STRUKTUR UTAMA (LAYER STACKING) ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer, // Atas
                        MaterialTheme.colorScheme.primaryContainer, // Tengah
                        MaterialTheme.colorScheme.surface           // Bawah
                    )
                )
            )
    ) {

        // ==========================================================
        // LAYER 1: BACKGROUND HEADER (GAMBAR)
        // ==========================================================
        // Pastikan HeroSection.kt Anda sudah menerima parameter 'modifier'
        // seperti pada instruksi sebelumnya.
        HeroSection(
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // ==========================================================
        // LAYER 2: KONTEN UTAMA (LEMBARAN MELENGKUNG)
        // ==========================================================
        Surface(
            modifier = Modifier
                .fillMaxSize()
                // PENTING: Turunkan lembaran ini 270dp agar gambar Hero (300dp) terlihat sebagian
                .padding(top = 270.dp)
                // PENTING: Buat sudut atas melengkung
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
                // Memberi jarak agar Stepper tidak nempel di ujung lengkungan
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 2. STEPPER SECTION
                item {
                    StepperSection(activeStep = 1)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 3. HEADER PILIH GEJALA
                item {
                    Text(
                        text = "Pilih Gejala",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }

                // 4. SEARCH BAR
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text("Cari gejala (misal: demam)", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                disabledContainerColor = MaterialTheme.colorScheme.surface,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (serverSymptoms.isEmpty()) {
                            Text(
                                text = "Gagal memuat daftar gejala. Mohon restart aplikasi.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else if (searchQuery.isBlank()) {
                            Text(
                                text = "Menampilkan 5 gejala teratas. Ketik untuk mencari lainnya.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else if (displayedGejala.isEmpty()) {
                            Text(
                                text = "Gejala tidak ditemukan.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        if (uiState.error != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error ?: "",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 5. LIST GEJALA
                items(displayedGejala) { symptomItem ->
                    val isSelected = uiState.selectedGejala.contains(symptomItem.id)

                    val containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        border = BorderStroke(1.dp, borderColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                            .clickable { viewModel.toggleGejala(symptomItem.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { viewModel.toggleGejala(symptomItem.id) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.onPrimary,
                                    checkmarkColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = symptomItem.label,
                                style = MaterialTheme.typography.bodyMedium,
                                color = contentColor,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // 6. TOMBOL NAVIGASI
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        shadowElevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedButton(
                                onClick = onBackClick,
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Kembali")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = { viewModel.predict() },
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.weight(1f),
                                enabled = uiState.selectedGejala.isNotEmpty() && !uiState.isLoading
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Analisa")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}