package com.example.healthcare.ui.screens.artikel

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    val scrollState = rememberScrollState()

    // KITA GANTI SCAFFOLD DENGAN BOX UTAMA (IMMERSIVE LAYOUT)
    Box(modifier = Modifier.fillMaxSize()) {

        // === LAYER 1: KONTEN SCROLLABLE ===
        if (article != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // --- HEADER IMAGE (Full Screen Width) ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp) // Tinggi gambar dibuat megah
                ) {
                    if (article.urlToImage != null) {
                        AsyncImage(
                            model = article.urlToImage,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize().background(Color.Gray))
                    }

                    // Gradient Bawah (Supaya Judul Terbaca)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                                    startY = 400f
                                )
                            )
                    )

                    // Judul & Metadata di atas Gambar
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 20.dp, vertical = 40.dp) // Padding bawah dilebihkan dikit
                    ) {
                        // Row Kategori & Sumber
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = article.category ?: "Umum",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = article.sourceName ?: "News",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = article.title ?: "Tanpa Judul",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            lineHeight = 32.sp
                        )
                    }
                }

                // --- BODY CONTENT (Putih) ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-24).dp) // Efek Overlap ke atas gambar
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(24.dp)
                ) {
                    // Visual Handle (Garis kecil)
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color.Gray.copy(alpha = 0.3f))
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Info Penulis & Tanggal
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = article.author ?: "Redaksi",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Default.DateRange, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = article.publishedAt?.take(10) ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Deskripsi & Konten
                    Text(
                        text = article.description ?: "Tidak ada deskripsi.",
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!article.content.isNullOrEmpty()) {
                        Text(
                            text = article.content,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Tombol Web
                    Button(
                        onClick = {
                            if (!article.url.isNullOrEmpty()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                context.startActivity(intent)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Baca Selengkapnya")
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Link, null)
                    }

                    Spacer(modifier = Modifier.height(50.dp)) // Spacer Bawah
                }
            }
        } else {
            // State Error / Not Found
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Artikel tidak ditemukan", color = Color.Gray)
            }
        }

        // === LAYER 2: NAVIGATION BAR & SCRIM (OVERLAY) ===
        // Ini rahasia tampilan modern agar Jam/Baterai tetap terlihat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // Gradient Hitam di Atas (Scrim)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent),
                        startY = 0f,
                        endY = 150f
                    )
                )
                .statusBarsPadding() // Turunkan agar tidak nabrak jam
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.2f), CircleShape) // Efek Kaca (Glass)
                    .size(40.dp)
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