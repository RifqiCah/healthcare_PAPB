package com.example.healthcare.data.model

import com.google.gson.annotations.SerializedName

/**
 * 1. Model utama untuk respons keseluruhan dari News API.
 * Ini adalah kelas yang akan dikembalikan oleh Retrofit.
 */
data class NewsApiResponse(
    // Status respons (misalnya "ok")
    @SerializedName("status")
    val status: String,

    // Jumlah total artikel yang ditemukan
    @SerializedName("totalResults")
    val totalResults: Int,

    // Daftar artikel yang sebenarnya
    @SerializedName("articles")
    val articles: List<ArticleDto>
)

/**
 * 2. Data Transfer Object (DTO) untuk setiap artikel.
 * Struktur ini mencerminkan objek JSON di dalam array "articles".
 */
data class ArticleDto(
    // Sumber artikel (objek nested)
    @SerializedName("source")
    val source: SourceDto?,

    // Penulis artikel
    @SerializedName("author")
    val author: String?,

    // Judul artikel
    @SerializedName("title")
    val title: String?,

    // Deskripsi singkat
    @SerializedName("description")
    val description: String?,

    // URL ke artikel lengkap
    @SerializedName("url")
    val url: String?,

    // URL ke gambar utama artikel
    @SerializedName("urlToImage")
    val urlToImage: String?,

    // Tanggal publikasi (ISO 8601 format)
    @SerializedName("publishedAt")
    val publishedAt: String?,

    // Konten (biasanya sebagian dari teks)
    @SerializedName("content")
    val content: String?
)

/**
 * 3. Model untuk Sumber (SourceDto).
 * Ini adalah objek yang bersarang (nested) di dalam ArticleDto.
 */
data class SourceDto(
    // ID sumber (bisa null/kosong)
    @SerializedName("id")
    val id: String?,

    // Nama sumber (misalnya "CNN", "Tribunnews")
    @SerializedName("name")
    val name: String?
)