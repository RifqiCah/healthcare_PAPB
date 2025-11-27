package com.example.healthcare.di

import com.example.healthcare.data.remote.SistemPakarApi
import com.example.healthcare.data.repository.SistemPakarRepositoryImpl
import com.example.healthcare.domain.repository.SistemPakarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SistemPakarModule {

    private const val FLASK_BASE_URL = "http://10.0.2.2:5000/"

    @Provides
    @Singleton
    @Named("FlaskOkHttpClient")
    fun provideFlaskOkHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("FlaskRetrofit")
    fun provideFlaskRetrofit(
        @Named("FlaskOkHttpClient") client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(FLASK_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideSistemPakarApi(
        @Named("FlaskRetrofit") retrofit: Retrofit
    ): SistemPakarApi =
        retrofit.create(SistemPakarApi::class.java)

    @Provides
    @Singleton
    fun provideSistemPakarRepository(api: SistemPakarApi): SistemPakarRepository =
        SistemPakarRepositoryImpl(api)
}
