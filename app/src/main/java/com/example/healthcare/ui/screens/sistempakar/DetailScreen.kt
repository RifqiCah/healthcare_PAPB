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
import coil.compose.AsyncImage // Pastikan dependency Coil sudah ada di build.gradle
import com.example.healthcare.domain.model.Article
import com.example.healthcare.viewmodel.SistemPakarViewModel

// Pastikan komponen HeaderSection, HeroSection, StepperSection tersedia/diimport
// jika error merah, sesuaikan importnya dengan lokasi file kamu

@Composable
fun DetailScreen(
    itemId: String?,
    viewModel: SistemPakarViewModel, // Parameter ViewModel Shared
    onSelesaiClick: () -> Unit,
    onBackClick: () -> Unit
) {
    // Ambil data dari Shared ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val hasilDiagnosa = uiState.hasilDiagnosa
    val articles = uiState.relatedArticles
    val isArticleLoading = uiState.isArticlesLoading

    val context = LocalContext.current

    // Ambil nama penyakit ranking 1 untuk ditampilkan di judul
    val topDiseaseName = hasilDiagnosa?.kemungkinan?.firstOrNull()?.nama ?: "Tidak Diketahui"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- STEPPER & HEADER ---
        // Asumsi fungsi-fungsi ini ada di project kamu
         HeaderSection()
         Spacer(Modifier.height(16.dp))
         HeroSection()
         Spacer(Modifier.height(24.dp))

//         Stepper aktif di langkah 4 (Detail/Saran)
         StepperSection(activeStep = 4)
         Spacer(Modifier.height(24.dp))

        Text(
            text = "Rekomendasi & Artikel",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        // --- INFO PENYAKIT UTAMA ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Berdasarkan diagnosa:", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = topDiseaseName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1565C0)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Berikut adalah artikel kesehatan yang relevan untuk membantu Anda memahami kondisi ini lebih lanjut.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Artikel Terkait:", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        // --- LIST ARTIKEL ---
        if (isArticleLoading) {
            Box(Modifier.fillMaxWidth().height(150.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (articles.isEmpty()) {
            Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                Text("Tidak ada artikel ditemukan.", color = Color.Gray, fontSize = 14.sp)
            }
        } else {
            // LazyColumn untuk menampilkan list artikel
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles) { article ->
                    ArticleItemSmall(
                        article = article,
                        onClick = {
                            // Buka link artikel di Browser
                            if (!article.url.isNullOrEmpty()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                context.startActivity(intent)
                            }
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --- TOMBOL NAVIGASI ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = onBackClick, shape = CircleShape, modifier = Modifier.weight(1f)) {
                Text("Kembali")
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = onSelesaiClick,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.weight(1f)
            ) {
                Text("Selesai", color = Color.White)
            }
        }
    }
}

// --- KOMPONEN ITEM ARTIKEL ---
@Composable
fun ArticleItemSmall(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            // Gambar Artikel
            AsyncImage(
                model = article.urlToImage ?: "https://via.placeholder.com/150",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Teks Artikel
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(80.dp)
            ) {
                Text(
                    text = article.title ?: "Tanpa Judul",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = article.sourceName ?: "News",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}