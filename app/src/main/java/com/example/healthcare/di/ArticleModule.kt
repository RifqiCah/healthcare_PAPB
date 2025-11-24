package com.example.healthcare.di

import com.example.healthcare.data.remote.NewsApiService // <-- Perhatikan ejaan (ApiService, bukan APIService)
import com.example.healthcare.data.repository.ArticleRepositoryImpl
import com.example.healthcare.domain.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent // <-- Ini solusi error SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named // <-- Ini solusi error Annotation
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArticleModule {

    private const val NEWS_BASE_URL = "https://newsapi.org/v2/"

    @Provides
    @Singleton
    @Named("NewsRetrofit")
    fun provideNewsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(@Named("NewsRetrofit") retrofit: Retrofit): NewsApiService {
        // Perhatikan bagian dalam kurung: NewsApiService::class.java
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleRepository(api: NewsApiService): ArticleRepository {
        return ArticleRepositoryImpl(api)
    }
}