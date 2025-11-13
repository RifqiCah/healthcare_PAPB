// Di dalam file /ui/screens/artikel/ArtikelScreen.kt
package com.example.healthcare.ui.screens.artikel

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtikelScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onArtikelDetailClick: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var currentPage by remember { mutableStateOf(1) }
    var isSearchActive by remember { mutableStateOf(false) }

    val categories = listOf("Semua", "Gaya Hidup", "Nutrisi", "Olahraga", "Mental Health", "Penyakit")

    val dummyArticles = listOf(
        ArticleItem("Aussie blokes urged to act F.A.S.T by learning the signs of stroke", "Gaya Hidup", "5 min read", "2 hari lalu"),
        ArticleItem("Simple blood test detects cancer up to 3 years before symptoms appear", "Penyakit", "8 min read", "3 hari lalu"),
        ArticleItem("How a purge at one obscure panel could disrupt vaccinations", "Penyakit", "6 min read", "1 minggu lalu"),
        ArticleItem("5 Tips Gaya Hidup Sehat untuk Mahasiswa", "Gaya Hidup", "4 min read", "2 minggu lalu"),
        ArticleItem("Mengenal Pola Makan Seimbang bagi Kesehatan Tubuh", "Nutrisi", "7 min read", "3 minggu lalu"),
        ArticleItem("Manfaat Olahraga Rutin untuk Kesehatan Jantung", "Olahraga", "5 min read", "1 bulan lalu"),
        ArticleItem("Cara Mengelola Stres di Tempat Kerja", "Mental Health", "6 min read", "1 bulan lalu")
    )

    val filteredArticles = remember(selectedCategory, searchQuery) {
        dummyArticles.filter { article ->
            val matchesCategory = selectedCategory == "Semua" || article.category == selectedCategory
            val matchesSearch = searchQuery.isEmpty() ||
                    article.title.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        /** --- Enhanced Hero Section --- **/
        item {
            AnimatedHeroSection()
            Spacer(modifier = Modifier.height(24.dp))
        }

        /** --- Modern Search Bar --- **/
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

        /** --- Category Chips --- **/
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    CategoryChip(
                        text = category,
                        isSelected = category == selectedCategory,
                        onClick = { selectedCategory = category }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        /** --- Results Header --- **/
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Artikel Terbaru",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = "${filteredArticles.size} artikel",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        /** --- Animated Article List --- **/
        itemsIndexed(filteredArticles) { index, article ->
            AnimatedArticleCard(
                article = article,
                index = index,
                onClick = { onArtikelDetailClick(article.title) }
            )
        }

        /** --- Enhanced Pagination --- **/
        if (filteredArticles.isNotEmpty()) {
            item {
                ModernPagination(
                    currentPage = currentPage,
                    totalPages = 3,
                    onPageChange = { currentPage = it }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/** --- DATA CLASS --- **/
// Jika ArticleData sudah ada di file lain, hapus bagian ini
// dan sesuaikan import-nya
data class ArticleItem(
    val title: String,
    val category: String,
    val readTime: String,
    val publishedTime: String
)

/** --- ANIMATED HERO SECTION --- **/
@Composable
fun AnimatedHeroSection() {
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_artikel),
            contentDescription = "Header Artikel",
            modifier = Modifier
                .fillMaxSize()
                .scale(scale),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📚 Blog Kesehatan",
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

/** --- MODERN SEARCH BAR --- **/
@OptIn(ExperimentalMaterial3Api::class)
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
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        tonalElevation = 2.dp
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
                            "Cari artikel kesehatan...",
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

/** --- CATEGORY CHIP --- **/
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
        color = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/** --- ANIMATED ARTICLE CARD --- **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedArticleCard(
    article: ArticleItem,
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
        enter = fadeIn(animationSpec = tween(400)) +
                slideInVertically(
                    animationSpec = tween(400),
                    initialOffsetY = { it / 2 }
                )
    ) {
        var isPressed by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.97f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessMedium),
            label = "card scale"
        )

        Card(
            onClick = onClick,
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
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.article_img),
                        contentDescription = article.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = article.category,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
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
                        text = article.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = article.readTime,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = article.publishedTime,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/** --- MODERN PAGINATION --- **/
@Composable
fun ModernPagination(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
    ) {
        // Previous Button
        IconButton(
            onClick = { if (currentPage > 1) onPageChange(currentPage - 1) },
            enabled = currentPage > 1
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Previous",
                tint = if (currentPage > 1)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Page Numbers
        repeat(totalPages) { index ->
            val page = index + 1
            val isCurrentPage = page == currentPage

            val scale by animateFloatAsState(
                targetValue = if (isCurrentPage) 1.1f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = "page scale"
            )

            Surface(
                onClick = { onPageChange(page) },
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .scale(scale),
                shape = CircleShape,
                color = if (isCurrentPage)
                    MaterialTheme.colorScheme.primary
                else
                    Color.Transparent
            ) {
                Text(
                    text = page.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isCurrentPage)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isCurrentPage) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Next Button
        IconButton(
            onClick = { if (currentPage < totalPages) onPageChange(currentPage + 1) },
            enabled = currentPage < totalPages
        ) {
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Next",
                tint = if (currentPage < totalPages)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewArtikelScreen() {
    MaterialTheme {
        ArtikelScreen(
            onBackClick = {},
            onArtikelDetailClick = {}
        )
    }
}