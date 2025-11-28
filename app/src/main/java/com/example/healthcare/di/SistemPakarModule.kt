package com.example.healthcare.di

import com.example.healthcare.data.repository.SistemPakarRepositoryImpl
import com.example.healthcare.domain.repository.SistemPakarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SistemPakarModule {

    // Fungsi ini memberitahu Hilt:
    // "Kalau ada yang minta SistemPakarRepository, tolong kasih SistemPakarRepositoryImpl"
    @Binds
    @Singleton
    abstract fun bindSistemPakarRepository(
        sistemPakarRepositoryImpl: SistemPakarRepositoryImpl
    ): SistemPakarRepository

}