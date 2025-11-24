package com.example.healthcare.domain.repository

import com.example.healthcare.domain.model.Diagnosa // <--- Import Model Domain (Bukan DTO lagi)

interface SistemPakarRepository {
    // Return type berubah: Dari PredictionResponse MENJADI Diagnosa
    suspend fun getPrediction(gejala: List<String>): Result<Diagnosa>
}