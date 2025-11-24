package com.example.healthcare.domain.repository

import com.example.healthcare.domain.model.Article

/**
 * Kontrak (Interface) untuk Repository Artikel.
 */
interface ArticleRepository {

    /**
     * Mengambil daftar artikel.
     * UPDATE: Sekarang menerima pageToken dan mengembalikan Pair(Artikel, NextPageToken).
     */
    suspend fun getArticlesByCategory(
        categoryKeyword: String,
        pageToken: String? = null
    ): Result<Pair<List<Article>, String?>> // <--- PERUBAHAN DISINI

    // Mengambil satu artikel dari cache memori
    fun getArticleById(id: String): Article?
}