package com.example.healthcare.ui.screens.artikel

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.healthcare.R
import com.example.healthcare.domain.model.Article
import com.example.healthcare.viewmodel.ArticleViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtikelScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onArtikelDetailClick: (String) -> Unit,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    val categories = listOf("Semua", "Gaya Hidup", "Nutrisi", "Olahraga", "Mental", "Penyakit")

    val displayedArticles = remember(uiState.articles, searchQuery) {
        if (searchQuery.isBlank()) {
            uiState.articles
        } else {
            uiState.articles.filter { article ->
                article.title?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    // --- STRUKTUR UTAMA (LAYER STACKING) ---
    Box(
        modifier = modifier
            .fillMaxSize()

    ) {

        // ==========================================================
        // LAYER 1: BACKGROUND HEADER (GAMBAR)
        // ==========================================================
        AnimatedHeroSection(
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // ==========================================================
        // LAYER 2: KONTEN UTAMA (LEMBARAN MELENGKUNG)
        // ==========================================================
        Surface(
            modifier = Modifier
                .fillMaxSize()
                // Turunkan 270dp agar gambar Hero (300dp) terlihat sebagian
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
            color = Color.Transparent
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                // Beri padding bawah agar konten terakhir tidak tertutup navigation bar (jika ada)
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 1. SPACER ATAS (Agar konten tidak nempel lengkungan)
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 2. SEARCH BAR
                item {
                    ModernSearchBar(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        isActive = isSearchActive,
                        onActiveChange = { isSearchActive = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // 3. CATEGORY CHIPS
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            CategoryChip(
                                text = category,
                                isSelected = category == uiState.selectedCategory,
                                onClick = { viewModel.fetchArticles(category) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 4. CONTENT LOGIC
                if (uiState.isLoading && uiState.articles.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                } else if (uiState.error != null && uiState.articles.isEmpty()) {
                    item {
                        ErrorSection(
                            message = uiState.error ?: "Terjadi Kesalahan",
                            onRetry = { viewModel.refresh() }
                        )
                    }
                } else {
                    // Header Text
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Berita Terbaru",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "${displayedArticles.size} artikel",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (displayedArticles.isEmpty()) {
                        item {
                            Text(
                                text = "Tidak ada artikel ditemukan.",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }

                    // Article List
                    itemsIndexed(displayedArticles) { index, article ->
                        AnimatedArticleCard(
                            article = article,
                            index = index,
                            onClick = { onArtikelDetailClick(article.id) }
                        )
                    }

                    // Load More Button
                    item {
                        if (uiState.nextPageToken != null && searchQuery.isBlank()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    OutlinedButton(
                                        onClick = { viewModel.loadMore() },
                                        shape = RoundedCornerShape(50),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = MaterialTheme.colorScheme.primary
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                                    ) {
                                        Text("Muat Lebih Banyak")
                                    }
                                }
                            }
                        }
                        // Spacer bawah extra agar scroll bisa mentok
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

/** --- ANIMATED HERO SECTION (BACKGROUND) --- **/
@Composable
fun AnimatedHeroSection(
    modifier: Modifier = Modifier // Ditambahkan parameter modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "hero")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Box ini sekarang menjadi Layer Belakang (Persegi Panjang Penuh)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
        // Hapus .clip() agar tidak melengkung di bawah
    ) {
        // 1. GAMBAR BACKGROUND
        Image(
            painter = painterResource(id = R.drawable.bg_artikel),
            contentDescription = "Header Artikel",
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentScale = ContentScale.Crop
        )

        // 2. GRADIENT OVERLAY (Agar teks terbaca)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.3f)
                        )
                    )
                )
        )

        // 3. TEKS KONTEN
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Artikel Kesehatan",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Informasi Terpercaya untuk Hidup Lebih Sehat",
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- KOMPONEN LAINNYA (SAMA SEPERTI SEBELUMNYA) ---

@Composable
fun ErrorSection(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Coba Lagi", color = MaterialTheme.colorScheme.onError)
        }
    }
}

@Composable
fun ModernSearchBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), // Sedikit transparan
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    if (searchQuery.isEmpty()) {
                        Text(
                            "Cari judul artikel...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            )
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "chip scale"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier.scale(animatedScale),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedArticleCard(
    article: Article,
    index: Int,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 50L)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(400)) + slideInVertically(
            animationSpec = tween(400),
            initialOffsetY = { it / 2 }
        )
    ) {
        // Variabel ini hanya hidup di dalam blok AnimatedVisibility ini
        var isPressed by remember { mutableStateOf(false) }

        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.97f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessMedium),
            label = "card scale"
        )

        Card(
            onClick = {
                isPressed = true
                onClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .scale(scale),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Image Section
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    if (article.urlToImage != null) {
                        AsyncImage(
                            model = article.urlToImage,
                            contentDescription = article.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.article_img),
                            contentDescription = "Default Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = article.sourceName ?: "News",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Content Section
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(
                        text = article.title ?: "Tanpa Judul",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = article.author ?: "Admin",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.widthIn(max = 80.dp)
                            )
                        }

                        Text(
                            text = article.publishedAt?.take(10) ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // ✅ PERBAIKAN: LaunchedEffect dipindah ke SINI (masih di dalam blok AnimatedVisibility)
        LaunchedEffect(isPressed) {
            if (isPressed) {
                delay(100)
                isPressed = false
            }
        }
    }
}