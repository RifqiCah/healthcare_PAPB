// Di dalam file /ui/screens/artikel/ReadArtikelScreen.kt
package com.example.healthcare.ui.screens.artikel

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay

// Data class untuk detail artikel
data class ArticleDetailData(
    val title: String,
    val date: String,
    val author: String,
    val readTime: String,
    val category: String,
    val imageRes: Int,
    val content: List<String>, // Paragraf terpisah untuk animasi
    val tags: List<String>,
    val views: String,
    val likes: String
)

// Fungsi helper untuk mendapatkan data dummy
private fun getArticleDetails(id: String?): ArticleDetailData {
    val paragraph1 = """
        Kesehatan adalah aset paling berharga yang kita miliki. Di tengah kesibukan sebagai mahasiswa, 
        menjaga kesehatan sering kali terabaikan. Padahal, tubuh yang sehat adalah kunci kesuksesan 
        dalam menjalani aktivitas perkuliahan dan kehidupan sosial.
    """.trimIndent()

    val paragraph2 = """
        Gaya hidup sehat tidak harus rumit atau mahal. Dengan beberapa kebiasaan sederhana yang 
        diterapkan secara konsisten, kita dapat meningkatkan kualitas hidup secara signifikan. 
        Berikut adalah tips praktis yang bisa langsung Anda terapkan.
    """.trimIndent()

    val paragraph3 = """
        Pertama, pastikan Anda mendapatkan tidur yang cukup. Mahasiswa sering begadang untuk 
        mengerjakan tugas atau bersosialisasi, namun kurang tidur dapat menurunkan konsentrasi dan 
        daya tahan tubuh. Usahakan tidur 7-8 jam setiap malam.
    """.trimIndent()

    val paragraph4 = """
        Kedua, konsumsi makanan bergizi seimbang. Hindari terlalu banyak junk food dan minuman 
        bersoda. Perbanyak konsumsi sayuran, buah-buahan, dan protein. Jangan lewatkan sarapan 
        karena ini adalah energi utama untuk memulai hari.
    """.trimIndent()

    val paragraph5 = """
        Ketiga, luangkan waktu untuk berolahraga. Tidak perlu ke gym, cukup 30 menit jalan kaki, 
        jogging, atau bersepeda sudah sangat membantu. Olahraga tidak hanya menyehatkan tubuh, 
        tetapi juga meningkatkan mood dan mengurangi stres.
    """.trimIndent()

    return when (id) {
        "5 Tips Gaya Hidup Sehat untuk Mahasiswa" -> ArticleDetailData(
            title = id,
            date = "22 Agustus 2025",
            author = "Dr. Sarah Wijaya",
            readTime = "5 menit baca",
            category = "Gaya Hidup",
            imageRes = R.drawable.bg_artikel,
            content = listOf(paragraph1, paragraph2, paragraph3, paragraph4, paragraph5),
            tags = listOf("Kesehatan", "Mahasiswa", "Gaya Hidup", "Tips"),
            views = "1.2k",
            likes = "89"
        )
        "Mengenal Pola Makan Seimbang bagi Kesehatan Tubuh" -> ArticleDetailData(
            title = id,
            date = "21 Agustus 2025",
            author = "dr. Ahmad Hidayat, Sp.GK",
            readTime = "7 menit baca",
            category = "Nutrisi",
            imageRes = R.drawable.bg_demam,
            content = listOf(paragraph1, paragraph2, paragraph3, paragraph4, paragraph5),
            tags = listOf("Nutrisi", "Diet", "Kesehatan", "Makanan"),
            views = "2.5k",
            likes = "156"
        )
        else -> ArticleDetailData(
            title = id ?: "Artikel Tidak Ditemukan",
            date = "20 Agustus 2025",
            author = "Tim Redaksi",
            readTime = "5 menit baca",
            category = "Umum",
            imageRes = R.drawable.article_img,
            content = if (id == null)
                listOf("ID Artikel tidak valid.")
            else
                listOf(paragraph1, paragraph2, paragraph3),
            tags = listOf("Kesehatan", "Info"),
            views = "500",
            likes = "32"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadArtikelScreen(
    itemId: String?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val article = getArticleDetails(itemId)
    val scrollState = rememberScrollState()
    var isLiked by remember { mutableStateOf(false) }
    var isBookmarked by remember { mutableStateOf(false) }

    // Parallax effect untuk header image
    val headerOffset = remember {
        derivedStateOf {
            (scrollState.value * 0.5f).coerceAtMost(300f)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero Image Section dengan Parallax
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                Image(
                    painter = painterResource(id = article.imageRes),
                    contentDescription = "Header Artikel",
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = (-headerOffset.value).dp),
                    contentScale = ContentScale.Crop
                )

                // Gradient Overlay
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

                // Category Badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = article.category,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Content Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-32).dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Title
                    AnimatedTitle(text = article.title)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Article Meta Info
                    ArticleMetaInfo(article)

                    Spacer(modifier = Modifier.height(24.dp))

                    Divider(color = MaterialTheme.colorScheme.outlineVariant)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Content Paragraphs with Animation
                    article.content.forEachIndexed { index, paragraph ->
                        AnimatedParagraph(
                            text = paragraph,
                            index = index
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Tags Section
                    TagsSection(tags = article.tags)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Author Card
                    AuthorCard(
                        authorName = article.author,
                        date = article.date
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    ActionButtons(
                        isLiked = isLiked,
                        onLikeClick = { isLiked = !isLiked },
                        isBookmarked = isBookmarked,
                        onBookmarkClick = { isBookmarked = !isBookmarked },
                        likes = article.likes
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // Floating Back Button
        FloatingBackButton(
            onClick = onBackClick,
            scrollOffset = scrollState.value
        )
    }
}

@Composable
fun AnimatedTitle(text: String) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInVertically(
                    animationSpec = tween(500),
                    initialOffsetY = { 50 }
                )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 36.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ArticleMetaInfo(article: ArticleDetailData) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(400))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = article.author,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = article.date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = article.readTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Views Counter
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = article.views,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedParagraph(text: String, index: Int) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay((500 + index * 150L))
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInVertically(
                    animationSpec = tween(500),
                    initialOffsetY = { 30 }
                )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                lineHeight = 28.sp,
                fontSize = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.87f),
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun TagsSection(tags: List<String>) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(800)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(400))
    ) {
        Column {
            Text(
                text = "Tags",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.take(4).forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = "#$tag",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthorCard(authorName: String, date: String) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(400)) + scaleIn(
            animationSpec = tween(400),
            initialScale = 0.9f
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = authorName.first().toString(),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = authorName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Dipublikasikan $date",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    likes: String
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1200)
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(400)) + slideInVertically(
            animationSpec = tween(400),
            initialOffsetY = { 40 }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Like Button
            Button(
                onClick = onLikeClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLiked)
                        MaterialTheme.colorScheme.errorContainer
                    else
                        MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isLiked) Color.Red else MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isLiked) "Disukai ($likes)" else "Suka ($likes)",
                    color = if (isLiked) Color.Red else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Bookmark Button
            IconButton(
                onClick = onBookmarkClick,
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = if (isBookmarked)
                            MaterialTheme.colorScheme.tertiaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.AddCircle,
                    contentDescription = "Bookmark",
                    tint = if (isBookmarked)
                        MaterialTheme.colorScheme.tertiary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Share Button
            IconButton(
                onClick = { /* TODO: Share */ },
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun FloatingBackButton(
    onClick: () -> Unit,
    scrollOffset: Int
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (scrollOffset > 100)
            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        else
            Color.White.copy(alpha = 0.7f),
        animationSpec = tween(300),
        label = "bg color"
    )

    Surface(
        onClick = onClick,
        modifier = Modifier
            .padding(16.dp)
            .size(48.dp),
        shape = CircleShape,
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewReadArtikelScreen() {
    MaterialTheme {
        ReadArtikelScreen(
            itemId = "5 Tips Gaya Hidup Sehat untuk Mahasiswa",
            onBackClick = {}
        )
    }
}