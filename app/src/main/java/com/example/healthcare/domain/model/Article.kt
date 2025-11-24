package com.example.healthcare.domain.model

/**
 * Model Domain Bersih (Clean Domain Model) untuk Artikel.
 * Data ini sudah melalui proses mapping, divalidasi, dan siap ditampilkan di UI.
 * Model ini harus independen dari library luar (seperti Retrofit atau Firebase).
 */
data class Article(
    // ID unik yang stabil (bisa berupa URL atau hash dari URL)
    val id: String,

    // Judul artikel
    val title: String?,

    // Penulis artikel
    val author: String?,

    // Nama sumber/media artikel (misalnya "CNN Indonesia")
    val sourceName: String?,

    // Deskripsi singkat
    val description: String?,

    // URL ke artikel lengkap
    val url: String?,

    // URL ke gambar utama artikel
    val urlToImage: String?,

    // Tanggal publikasi (sudah diformat atau masih raw string, tergantung kebutuhan)
    val publishedAt: String?,

    // Konten (biasanya sebagian dari teks)
    val content: String?,
    //kategori(healthy)
    val category: String?
)