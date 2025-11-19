package com.example.healthcare.domain.repository

import com.example.healthcare.domain.model.Article

/**
 * Kontrak (Interface) untuk Repository Artikel.
 * Ini mendefinisikan operasi-operasi data yang tersedia untuk domain layer.
 */
interface ArticleRepository {

    /**
     * Mengambil daftar artikel dari berbagai sumber (API/cache) berdasarkan kata kunci.
     * Mengembalikan Result untuk menangani keberhasilan atau kegagalan operasi.
     */
    suspend fun getArticlesByCategory(categoryKeyword: String): Result<List<Article>>

    // Operasi lain bisa ditambahkan di sini, misalnya:
    // suspend fun getArticleDetail(articleId: String): Result<Article>
    // suspend fun saveArticleToCache(article: Article)
}