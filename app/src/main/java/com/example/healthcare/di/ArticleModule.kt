package com.example.healthcare.di

import com.example.healthcare.data.remote.NewsApiService
import com.example.healthcare.data.repository.ArticleRepositoryImpl
import com.example.healthcare.domain.repository.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ArticleModule {

    private const val NEWS_BASE_URL = "https://newsdata.io/api/1/"

    // PROVIDE OKHTTP UNTUK RETROFIT NEWS
    @Provides
    @Singleton
    @Named("NewsClient")
    fun provideNewsOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    // PROVIDE RETROFIT UNTUK NEWS
    @Provides
    @Singleton
    @Named("NewsRetrofit")
    fun provideNewsRetrofit(
        @Named("NewsClient") client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(NEWS_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // PROVIDE API SERVICE
    @Provides
    @Singleton
    fun provideNewsApiService(
        @Named("NewsRetrofit") retrofit: Retrofit
    ): NewsApiService =
        retrofit.create(NewsApiService::class.java)

    // PROVIDE REPOSITORY
    @Provides
    @Singleton
    fun provideArticleRepository(
        api: NewsApiService
    ): ArticleRepository =
        ArticleRepositoryImpl(api)
}
