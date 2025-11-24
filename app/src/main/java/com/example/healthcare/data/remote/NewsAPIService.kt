package com.example.healthcare.data.remote

import com.example.healthcare.data.model.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("news")
    suspend fun getArticles(
        // API Key wajib
        @Query("apikey") apiKey: String,

        // Filter Negara: Indonesia
        @Query("country") country: String = "id",

        // Filter Kategori: Kesehatan
        @Query("category") category: String = "health",

        // [PERBAIKAN 1] Filter Bahasa: Wajib Indonesia ("id")
        // Ini yang akan membuang berita bahasa China/Inggris
        @Query("language") language: String = "id",

        // [PERBAIKAN 2] Batasi jumlah artikel per request
        // Default 5, biar tidak terlalu banyak di awal
        @Query("size") size: Int = 5,

        // [PERBAIKAN 3] Parameter untuk Halaman Selanjutnya (Pagination)
        // Nanti diisi dengan token 'nextPage' kalau user klik "Load More"
        @Query("page") page: String? = null,

        // Pencarian (Opsional)
        @Query("q") query: String? = null
    ): NewsApiResponse
}