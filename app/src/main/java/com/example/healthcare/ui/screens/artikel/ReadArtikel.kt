// Di dalam file /ui/screens/artikel/ReadArtikelScreen.kt
// (Atau lebih baik, ganti nama file ini menjadi DetailArtikelScreen.kt)
package com.example.healthcare.ui.screens.artikel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R
import androidx.compose.ui.text.style.TextOverflow

// Data class dummy untuk detail artikel
data class ArticleData(
    val title: String,
    val date: String,
    val imageRes: Int,
    val content: String
)

// Fungsi helper untuk mendapatkan data dummy berdasarkan ID (judul)
private fun getArticleDetails(id: String?): ArticleData {
    val dummyContent = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
        Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
        Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
        
        Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. 
        Curabitur pretium tincidunt lacus. Nulla gravida orci a odio. Nullam varius, turpis et commodo pharetra, 
        est eros bibendum elit, nec luctus magna felis sollicitudin mauris. Integer in mauris eu nibh euismod gravida.
    """.trimIndent()

    return when (id) {
        "5 Tips Gaya Hidup Sehat untuk Mahasiswa" -> ArticleData(
            title = id,
            date = "22 Agustus 2025",
            imageRes = R.drawable.bg_artikel, // Ganti dengan gambar relevan
            content = "Konten untuk $id... $dummyContent"
        )
        "Mengenal Pola Makan Seimbang bagi Kesehatan Tubuh" -> ArticleData(
            title = id,
            date = "21 Agustus 2025",
            imageRes = R.drawable.bg_demam, // Ganti dengan gambar relevan
            content = "Konten untuk $id... $dummyContent"
        )
        else -> ArticleData(
            title = id ?: "Artikel Tidak Ditemukan",
            date = "20 Agustus 2025",
            imageRes = R.drawable.article_img, // Gambar default
            content = if (id == null) "ID Artikel tidak valid." else "Konten untuk $id... $dummyContent"
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadArtikelScreen(
    // UBAH INI: Terima itemId dan onBackClick
    itemId: String?,
    onBackClick: () -> Unit
) {
    // Ambil data berdasarkan ID.
    // Di aplikasi nyata, ini akan memanggil ViewModel: viewModel.getArticleById(itemId)
    val article = getArticleDetails(itemId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Baca Artikel",
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    // UBAH INI: Gunakan lambda onBackClick
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = article.date,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = article.imageRes),
                contentDescription = "Gambar Artikel",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = article.content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewReadArtikelScreen() {
    MaterialTheme {
        // Beri ID dummy untuk preview
        ReadArtikelScreen(
            itemId = "5 Tips Gaya Hidup Sehat untuk Mahasiswa",
            onBackClick = {}
        )
    }
}