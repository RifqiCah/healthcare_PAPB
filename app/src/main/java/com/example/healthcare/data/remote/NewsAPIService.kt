package com.example.healthcare.data.remote

import com.example.healthcare.data.remote.model.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface Retrofit untuk mendefinisikan endpoint News API.
 * Base URL: https://newsapi.org/
 */
interface NewsApiService {

    @GET("v2/everything")
    suspend fun getArticles(
        // @Query("q"): Kata kunci untuk pencarian, misalnya "nutrition OR diet"
        @Query("q") query: String,

        // @Query("language"): Filter bahasa. Default ke Bahasa Indonesia
        @Query("language") language: String = "id",

        // @Query("sortBy"): Urutan berdasarkan tanggal publikasi
        @Query("sortBy") sortBy: String = "publishedAt",

        // @Query("apiKey"): Kunci API Anda
        @Query("apiKey") apiKey: String

    ): NewsApiResponse
}