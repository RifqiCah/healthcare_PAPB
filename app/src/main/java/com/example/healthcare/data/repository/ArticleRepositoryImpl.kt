package com.example.healthcare.data.repository

import com.example.healthcare.BuildConfig
import com.example.healthcare.data.model.ArticleDto
import com.example.healthcare.data.remote.NewsApiService
import com.example.healthcare.domain.model.Article
import com.example.healthcare.domain.repository.ArticleRepository
import javax.inject.Inject

/**
 * Implementasi Repository untuk Artikel.
 * Menggunakan @Inject agar Hilt bisa menyuntikkan NewsApiService secara otomatis.
 */
class ArticleRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService
) : ArticleRepository {

    override suspend fun getArticlesByCategory(categoryKeyword: String): Result<List<Article>> {
        return try {
            // 1. Panggil API
            // Pastikan parameter 'query' dan 'apiKey' sesuai dengan definisi di NewsApiService
            val response = apiService.getArticles(
                query = categoryKeyword,
                apiKey = BuildConfig.NEWS_API_KEY
            )

            // 2. Cek Status Logis dari API (NewsAPI biasanya mengembalikan field "status": "ok")
            if (response.status == "ok") {

                // 3. Mapping (DTO -> Domain)
                // Filter artikel yang dihapus/rusak (kadang NewsAPI kasih article dengan title "[Removed]")
                val articles = response.articles
                    .filter { it.title != "[Removed]" && it.url != null }
                    .map { it.toArticle() }

                Result.success(articles)
            } else {
                // API merespon (200 OK) tapi status datanya error
                Result.failure(Exception("API Error: Status not OK"))
            }

        } catch (e: Exception) {
            // Error Jaringan, Timeout, atau HTTP Error (401, 404, 500)
            Result.failure(e)
        }
    }
}

// ===============================================
// EXTENSION FUNCTION: MAPPER
// ===============================================

fun ArticleDto.toArticle(): Article {
    // Trik membuat ID unik: Gunakan hash dari URL. Jika URL kosong, pakai hash Judul + Waktu.
    val uniqueId = this.url?.hashCode()?.toString()
        ?: "${this.title}${this.publishedAt}".hashCode().toString()

    return Article(
        id = uniqueId,
        title = this.title ?: "Tanpa Judul",
        author = this.author ?: "Unknown",
        sourceName = this.source?.name ?: "Sumber Tidak Diketahui",
        description = this.description ?: "Tidak ada deskripsi",
        url = this.url ?: "",
        urlToImage = this.urlToImage, // Boleh null, nanti dihandle di UI (pake placeholder)
        publishedAt = this.publishedAt ?: "",
        content = this.content ?: ""
    )
}