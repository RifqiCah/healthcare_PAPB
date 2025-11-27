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

    // Ambil nama penyakit ranking 1 untuk ditampilkan di judul
    val topDiseaseName = hasilDiagnosa?.kemungkinan?.firstOrNull()?.nama ?: "Tidak Diketahui"

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. HERO SECTION (Edge-to-edge)
            item {
                HeroSection()
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 2. STEPPER SECTION
            item {
                StepperSection(activeStep = 3) // Step 3 = Detail
                Spacer(modifier = Modifier.height(32.dp))
            }

            // 3. HEADER TEXT
//            item {
//                Text(
//                    text = "Rekomendasi & Artikel",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF00BCD4),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 20.dp)
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//            }

            // 4. INFO PENYAKIT UTAMA
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE0F7FA) // Light Cyan
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
                            // Badge
                            Surface(
                                shape = CircleShape,
                                color = Color.White,
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
                                    "Hasil Diagnosa:",
                                    fontSize = 12.sp,
                                    color = Color(0xFF00838F),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = topDiseaseName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00838F)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = Color(0xFF00BCD4).copy(alpha = 0.2f))

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Berikut adalah artikel kesehatan yang relevan untuk membantu Anda memahami kondisi ini lebih lanjut.",
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = Color(0xFF00838F)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // 5. HEADER ARTIKEL
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Artikel Terkait",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (!isArticleLoading && articles.isNotEmpty()) {
                        Text(
                            "${articles.size} artikel",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // 6. LIST ARTIKEL
            if (isArticleLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF00BCD4))
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
                            containerColor = Color(0xFFF5F5F5)
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
                                    "Tidak ada artikel ditemukan",
                                    color = Color.Gray,
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

            // 7. TOMBOL NAVIGASI
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shadowElevation = 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White
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
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Kembali", color = Color(0xFF00BCD4))

                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = onSelesaiClick,
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00BCD4)
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Selesai", color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

// --- KOMPONEN ITEM ARTIKEL (IMPROVED) ---
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
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Gambar Artikel
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5))
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
                    color = Color(0xFF00BCD4).copy(alpha = 0.9f)
                ) {
                    Text(
                        text = article.sourceName?.take(8) ?: "News",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                        fontSize = 10.sp,
                        color = Color.White,
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
                    lineHeight = 18.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.publishedAt?.take(10) ?: "",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = "Open",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF00BCD4)
                    )
                }
            }
        }
    }
}