package com.example.healthcare.ui.screens.artikel

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.healthcare.viewmodel.ArticleViewModel

@Composable
fun ReadArtikelScreen(
    itemId: String?,
    onBackClick: () -> Unit,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val article = remember(itemId) {
        itemId?.let { viewModel.getArticleDetail(it) }
    }
    val context = LocalContext.current

    // --- STRUKTUR UTAMA (LAYER STACKING - Qpon Style) ---
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
        if (article != null) {
            // ==========================================================
            // LAYER 1: BACKGROUND IMAGE
            // ==========================================================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp) // Tinggi gambar background
                    .align(Alignment.TopCenter)
            ) {
                if (article.urlToImage != null) {
                    AsyncImage(
                        model = article.urlToImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    )
                }

                // Gradient Overlay (Agar teks kategori di atas gambar terbaca)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 300f // Mulai gelap dari tengah ke bawah
                            )
                        )
                )

                // Info Kategori & Source (Tampil di atas gambar, di belakang Sheet)
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 60.dp) // Padding besar agar muncul di atas Sheet
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = article.category ?: "Kesehatan",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = article.sourceName ?: "News",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ==========================================================
            // LAYER 2: KONTEN UTAMA (LEMBARAN MELENGKUNG)
            // ==========================================================
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    // Turunkan sheet agar gambar terlihat di atasnya
                    .padding(top = 360.dp)
                    // Lengkungan sudut atas
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
                color = MaterialTheme.colorScheme.background
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(24.dp)
                ) {
                    // 1. Handle Bar (Garis kecil visual)
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(40.dp)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // 2. JUDUL ARTIKEL
                    item {
                        Text(
                            text = article.title ?: "Tanpa Judul",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 32.sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 3. METADATA (Penulis & Tanggal)
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = article.author ?: "Redaksi",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = article.publishedAt?.take(10) ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // 4. ISI KONTEN
                    item {
                        if (!article.description.isNullOrEmpty()) {
                            Text(
                                text = article.description,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 26.sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        if (!article.content.isNullOrEmpty()) {
                            Text(
                                text = article.content,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = 28.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "Baca selengkapnya di situs asli...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    // 5. TOMBOL WEB
                    item {
                        Button(
                            onClick = {
                                if (!article.url.isNullOrEmpty()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                    context.startActivity(intent)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = "Baca Selengkapnya",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.Link, null)
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                    }
                }
            }

        } else {
            // State Loading / Error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (article == null) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {
                    Text("Artikel tidak ditemukan", color = MaterialTheme.colorScheme.outline)
                }
            }
        }

        // ==========================================================
        // LAYER 3: NAVIGATION BAR (FLOATING)
        // ==========================================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(
                        color = Color.Black.copy(alpha = 0.4f), // Semi-transparent black
                        shape = CircleShape
                    )
                    .size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }
    }
}