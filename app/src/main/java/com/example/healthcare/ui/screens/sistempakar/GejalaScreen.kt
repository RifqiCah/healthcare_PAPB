package com.example.healthcare.ui.screens.sistempakar

import androidx.compose.foundation.BorderStroke
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

    LaunchedEffect(uiState.hasilDiagnosa) {
        if (uiState.hasilDiagnosa != null) {
            onLanjutClick()
        }
    }

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
                StepperSection(activeStep = 1)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 3. INPUT DATA DIRI DIHAPUS ---

            // 4. HEADER PILIH GEJALA
            item {
                Text(
                    text = "Pilih Gejala",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // 5. SEARCH BAR
            item {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari gejala (misal: demam, nyeri)") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            unfocusedBorderColor = Color.LightGray,
                            focusedBorderColor = Color(0xFF00BCD4),
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (searchQuery.isBlank()) {
                        Text(
                            text = "Menampilkan 5 gejala teratas. Ketik untuk mencari lainnya.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
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
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 6. LIST GEJALA
            items(displayedGejala) { symptomItem ->
                val isSelected = uiState.selectedGejala.contains(symptomItem.id)

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF00BCD4) else Color.White
                    ),
                    border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clickable { viewModel.toggleGejala(symptomItem.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { viewModel.toggleGejala(symptomItem.id) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = symptomItem.label,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // 7. TOMBOL NAVIGASI
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    shadowElevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onBackClick,
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Kembali", color = Color(0xFF00BCD4))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // --- PERBAIKAN DI SINI ---
                        Button(
                            onClick = { viewModel.predict() },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00BCD4)),
                            modifier = Modifier.weight(1f),
                            // SYARAT BUTTON ENABLED DIUBAH:
                            // Hanya cek apakah gejala sudah dipilih dan tidak loading
                            enabled = uiState.selectedGejala.isNotEmpty() && !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Analisa", color = Color.White)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}