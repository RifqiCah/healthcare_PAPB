package com.example.healthcare.data.repository

import com.example.healthcare.BuildConfig
import com.example.healthcare.data.model.ArticleDto
import com.example.healthcare.data.remote.NewsApiService
import com.example.healthcare.domain.model.Article
import com.example.healthcare.domain.repository.ArticleRepository
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService
) : ArticleRepository {

    // --- MEMORI SEMENTARA (CACHE) ---
    // Menyimpan SEMUA artikel yang sudah dimuat (Halaman 1 + 2 + dst)
    private var cachedArticles: List<Article> = emptyList()

    override suspend fun getArticlesByCategory(
        categoryKeyword: String,
        pageToken: String?
    ): Result<Pair<List<Article>, String?>> {
        return try {
            // Gunakan API Key (Bisa pakai BuildConfig atau Hardcode sementara)
            val myApiKey = "pub_3d90d6e66e784e2bb222e1ce7a92712d"

            // Logic Query: "health" -> null agar ambil semua
            val searchQuery = if (categoryKeyword == "health" || categoryKeyword.contains("health")) null else categoryKeyword

            val response = apiService.getArticles(
                apiKey = myApiKey,
                query = searchQuery,
                page = pageToken, // Kirim token halaman (bisa null)
                size = 5 // Minta 5 artikel per halaman
            )

            if (response.status == "success") {
                val newArticles = response.results
                    .filter { it.title != null }
                    .map { it.toArticle() }

                // --- UPDATE CACHE ---
                if (pageToken == null) {
                    // Halaman pertama: Reset cache
                    cachedArticles = newArticles
                } else {
                    // Halaman selanjutnya: Gabung dengan yang lama
                    cachedArticles = cachedArticles + newArticles
                }

                // Kembalikan: (Artikel Baru, Token Halaman Berikutnya)
                Result.success(Pair(newArticles, response.nextPage))
            } else {
                Result.failure(Exception("API Error: Status not success"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getArticleById(id: String): Article? {
        return cachedArticles.find { it.id == id }
    }
}

// ===============================================
// MAPPER (DTO -> DOMAIN)
// ===============================================

fun ArticleDto.toArticle(): Article {
    val authorString = this.creator?.joinToString(", ") ?: "Redaksi"

    // Gunakan ID asli dari API
    val uniqueId = this.articleId

    val categoryString = this.category?.firstOrNull()
        ?.replaceFirstChar { it.uppercase() }
        ?: "Kesehatan"

    return Article(
        id = uniqueId,
        title = this.title ?: "Tanpa Judul",
        author = authorString,
        sourceName = this.sourceId?.uppercase() ?: "NEWS",
        category = categoryString,
        description = this.description ?: "Klik tombol di bawah untuk membaca selengkapnya...",
        url = this.link ?: "",
        urlToImage = this.imageUrl,
        publishedAt = this.pubDate ?: "",
        content = this.content ?: ""
    )
}