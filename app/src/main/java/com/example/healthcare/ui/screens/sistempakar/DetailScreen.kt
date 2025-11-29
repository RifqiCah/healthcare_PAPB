package com.example.healthcare.ui.screens.sistempakar

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.healthcare.domain.model.Article
import com.example.healthcare.viewmodel.SistemPakarViewModel

@Composable
fun DetailScreen(
    itemId: String?,
    viewModel: SistemPakarViewModel,
    onSelesaiClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val hasilDiagnosa = uiState.hasilDiagnosa
    val articles = uiState.relatedArticles
    val isArticleLoading = uiState.isArticlesLoading

    val context = LocalContext.current

    val topDiseaseName = hasilDiagnosa?.firstOrNull()?.penyakit ?: "Tidak Diketahui"

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
                // PENTING: Turunkan 270dp agar gambar Hero (300dp) terlihat sebagian
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
                    StepperSection(activeStep = 3) // Step 3 = Detail
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 3. INFO PENYAKIT UTAMA (Top 1)
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            // Menggunakan primaryContainer (Cyan Muda di Light Mode)
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Badge Icon
                                Surface(
                                    shape = CircleShape,
                                    // Icon background putih/surface
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "🩺",
                                            fontSize = 20.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "Hasil Diagnosa Tertinggi:",
                                        fontSize = 12.sp,
                                        // Warna teks yang kontras di atas primaryContainer
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = topDiseaseName,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Divider(
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Berikut adalah artikel kesehatan yang relevan untuk membantu Anda memahami kondisi ini lebih lanjut.",
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 4. HEADER ARTIKEL
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Artikel Terkait",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        if (!isArticleLoading && articles.isNotEmpty()) {
                            Text(
                                text = "${articles.size} artikel",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // 5. LIST ARTIKEL
                if (isArticleLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                } else if (articles.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("📰", fontSize = 40.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Tidak ada artikel ditemukan",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                } else {
                    items(articles) { article ->
                        ArticleItemCard(
                            article = article,
                            onClick = {
                                if (!article.url.isNullOrEmpty()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                    context.startActivity(intent)
                                }
                            },
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                        )
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
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Kembali")
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = onSelesaiClick,
                                shape = RoundedCornerShape(50),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Selesai")
                            }
                        }
                    }
                    // Spacer bawah agar scroll bisa mentok sampai tombol
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

// --- KOMPONEN ITEM ARTIKEL (Tidak Berubah) ---
@Composable
fun ArticleItemCard(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Gambar Artikel
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant) // Placeholder warna
            ) {
                AsyncImage(
                    model = article.urlToImage ?: "https://via.placeholder.com/150",
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Badge Source
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                ) {
                    Text(
                        text = article.sourceName?.take(8) ?: "News",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Teks Artikel
            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = article.title ?: "Tanpa Judul",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.publishedAt?.take(10) ?: "",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Open",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}