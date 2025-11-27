package com.example.healthcare.di

import com.example.healthcare.data.remote.SistemPakarApi
import com.example.healthcare.data.repository.SistemPakarRepositoryImpl
import com.example.healthcare.domain.repository.SistemPakarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // Pastikan library ini ada di build.gradle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SistemPakarModule {

    // URL untuk Emulator Android Studio
    private const val FLASK_BASE_URL = "http://10.0.2.2:5000/"

    @Provides
    @Singleton
    @Named("FlaskOkHttpClient")
    fun provideFlaskOkHttpClient(): OkHttpClient {
        // 1. Buat Logger agar request & response muncul di Logcat (PENTING BUAT DEBUG)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            // 2. PERPANJANG TIMEOUT (Model AI kadang butuh waktu lama mikir)
            .connectTimeout(30, TimeUnit.SECONDS) // Waktu maksimal nyambung ke server
            .readTimeout(30, TimeUnit.SECONDS)    // Waktu maksimal nunggu balasan server
            .writeTimeout(30, TimeUnit.SECONDS)   // Waktu maksimal kirim data
            .build()
    }

    @Provides
    @Singleton
    @Named("FlaskRetrofit")
    fun provideFlaskRetrofit(@Named("FlaskOkHttpClient") client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FLASK_BASE_URL)
            .client(client) // Pasang client yang sudah kita setting di atas
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSistemPakarApi(@Named("FlaskRetrofit") retrofit: Retrofit): SistemPakarApi {
        return retrofit.create(SistemPakarApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSistemPakarRepository(api: SistemPakarApi): SistemPakarRepository {
        return SistemPakarRepositoryImpl(api)
    }
}