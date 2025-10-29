package com.example.healthcare.ui.screens.artikel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthcare.R

@Composable
fun ArtikelScreen() {
    var searchQuery by remember { mutableStateOf("") }

    val dummyArticles = listOf(
        "Aussie blokes urged to act F.A.S.T by learning the signs of stroke",
        "Simple blood test detects cancer up to 3 years before symptoms appear",
        "How a purge at one obscure panel could disrupt vaccinations",
        "5 Tips Gaya Hidup Sehat untuk Mahasiswa",
        "Mengenal Pola Makan Seimbang bagi Kesehatan Tubuh"
    )

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            /** --- Hero Section --- **/
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bg_artikel),
                        contentDescription = "Header Artikel",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Black.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "BLOG KESEHATAN",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "GAYA HIDUP SEHAT",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            /** --- Search Section --- **/
            item {
                Text(
                    text = "List Artikel Kesehatan",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari artikel...") },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 4.dp)
                )

                Button(
                    onClick = { /* TODO: search logic */ },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(vertical = 4.dp)
                ) {
                    Text("Cari")
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            /** --- List Artikel --- **/
            items(dummyArticles) { title ->
                ArtikelCard(
                    title = title,
                    description = "Dapatkan informasi terkini seputar kesehatan dan gaya hidup sehat dari sumber terpercaya.",
                    onClick = {}
                )
            }

            /** --- Pagination --- **/
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    IconButton(onClick = { /* Prev */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                    }

                    repeat(5) { i ->
                        Button(
                            onClick = { /* Page click */ },
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (i == 1)
                                    MaterialTheme.colorScheme.primary
                                else Color.LightGray
                            ),
                            modifier = Modifier
                                .size(36.dp)
                                .padding(horizontal = 4.dp)
                        ) {
                            Text("${i + 1}")
                        }
                    }

                    IconButton(onClick = { /* Next */ }) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/** --- KOMPONEN CARD ARTIKEL --- **/
@Composable
fun ArtikelCard(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text("Tidak ada gambar", color = Color.Gray, fontSize = 12.sp)
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onClick, shape = RoundedCornerShape(8.dp)) {
                    Text("Baca Artikel")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArtikelScreen() {
    ArtikelScreen()
}
