package com.example.healthcare.data.model

import com.google.gson.annotations.SerializedName

/**
 * 1. Response Utama (NewsData.io)
 */
data class NewsApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResults: Int,

    @SerializedName("results")
    val results: List<ArticleDto>,

    // PERBAIKAN 1: 'nextPage' harusnya ada di sini (Root Response), bukan di dalam artikel
    @SerializedName("nextPage")
    val nextPage: String?
)

/**
 * 2. Item Artikel (NewsData.io)
 */
data class ArticleDto(
    @SerializedName("article_id")
    val articleId: String,

    @SerializedName("title")
    val title: String?,

    @SerializedName("link")
    val link: String?,

    @SerializedName("creator")
    val creator: List<String>?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("content")
    val content: String?,

    @SerializedName("pubDate")
    val pubDate: String?,

    @SerializedName("image_url")
    val imageUrl: String?,

    @SerializedName("source_id")
    val sourceId: String?,

    // PERBAIKAN 2: Tambahkan koma di ujung sini
    @SerializedName("category")
    val category: List<String>?
)