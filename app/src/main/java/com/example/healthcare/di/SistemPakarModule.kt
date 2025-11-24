package com.example.healthcare.di

import com.example.healthcare.data.remote.SistemPakarApi
import com.example.healthcare.data.repository.SistemPakarRepositoryImpl
import com.example.healthcare.domain.repository.SistemPakarRepository
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
object SistemPakarModule {

    // GANTI URL INI:
    // Jika Emulator Android Studio: "http://10.0.2.2:5000/"
    // Jika HP Asli (Lewat Ngrok): "https://xxxx-xxxx.ngrok-free.app/"
    private const val FLASK_BASE_URL = "http://10.0.2.2:5000/"

    @Provides
    @Singleton
    @Named("FlaskRetrofit") // Kita kasih nama biar gak bentrok sama NewsAPI
    fun provideFlaskRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(FLASK_BASE_URL)
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