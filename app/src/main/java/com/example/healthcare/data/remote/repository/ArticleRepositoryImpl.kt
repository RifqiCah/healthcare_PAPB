package com.example.healthcare.data.remote.repository


import com.example.healthcare.BuildConfig // Diperlukan untuk mengakses NEWS_API_KEY
import com.example.healthcare.data.remote.NewsApiService
import com.example.healthcare.data.remote.model.ArticleDto
import com.example.healthcare.domain.model.Article
import com.example.healthcare.domain.repository.ArticleRepository
import javax.inject.Inject // Gunakan jika Anda memakai Dependency Injection (Hilt/Koin)

/**
 * Implementasi dari kontrak ArticleRepository.
 * Bertanggung jawab mengambil data dari NewsApiService dan memetakan (mapping) ke Model Domain.
 */
class ArticleRepositoryImpl @Inject constructor(
    // NewsApiService adalah interface Retrofit yang melakukan panggilan jaringan
    private val apiService: NewsApiService
    // Jika Anda menggunakan Hilt/Koin, @Inject akan menangani pembuatan objek ini
) : ArticleRepository {

    // Mengimplementasikan fungsi yang didefinisikan di interface ArticleRepository
    override suspend fun getArticlesByCategory(categoryKeyword: String): Result<List<Article>> {

        // Menggunakan Kotlin Result untuk menangani keberhasilan (Success) atau kegagalan (Failure)
        return try {

            // 1. Panggilan Jaringan (dilakukan secara asinkron/suspend)
            val response = apiService.getArticles(
                query = categoryKeyword,
                apiKey = BuildConfig.NEWS_API_KEY // Ambil kunci API dari BuildConfig
            )

            // Cek apakah API merespons dengan status OK
            if (response.status == "ok") {

                // 2. Mapping DTO ke Model Domain
                val articles = response.articles.map { it.toArticle() }

                // Mengembalikan hasil yang sukses dengan List<Article>
                Result.success(articles)
            } else {
                // Menangani jika respons API tidak OK (meski status HTTP 200)
                Result.failure(Exception("Gagal memuat artikel: Status API tidak OK."))
            }
        } catch (e: Exception) {
            // 3. Menangkap Error (jaringan, timeout, atau parsing JSON)
            Result.failure(Exception("Kesalahan Jaringan atau Parsing: ${e.message}"))
        }
    }
}

// ===============================================
// Fungsi Extension untuk Pemetaan (Mapper)
// ===============================================

/**
 * Fungsi ini bertugas mengkonversi ArticleDto (data dari API)
 * menjadi Article (model domain bersih untuk digunakan di UI/ViewModel).
 */
fun ArticleDto.toArticle(): Article {

    // Membuat ID unik berdasarkan URL, jika URL null, gunakan hash judul
    val uniqueId = this.url?.hashCode()?.toString() ?: this.title?.hashCode()?.toString() ?: ""

    return Article(
        id = uniqueId,
        title = this.title,
        author = this.author,
        sourceName = this.source?.name, // Akses nama dari SourceDto
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}