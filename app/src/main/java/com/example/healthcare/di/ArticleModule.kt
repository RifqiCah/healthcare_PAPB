package com.example.healthcare.di

import com.example.healthcare.data.remote.NewsApiService
import com.example.healthcare.data.repository.ArticleRepositoryImpl
import com.example.healthcare.domain.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArticleModule {

    // URL NewsData.io
    private const val NEWS_BASE_URL = "https://newsdata.io/api/1/"

    @Provides
    @Singleton
    @Named("NewsRetrofit") // Nama ini PENTING biar gak ketukar sama Flask
    fun provideNewsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApiService(@Named("NewsRetrofit") retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideArticleRepository(api: NewsApiService): ArticleRepository {
        return ArticleRepositoryImpl(api)
    }
}