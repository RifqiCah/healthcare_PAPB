package com.example.healthcare.domain.repository

import com.example.healthcare.data.model.SymptomItem
import com.example.healthcare.domain.model.Diagnosa

interface SistemPakarRepository {
    // Fungsi Prediksi (Lama)
    suspend fun getPrediction(gejala: List<String>): Result<Diagnosa>

    // Fungsi Ambil Gejala (BARU)
    // Kita return List<SymptomItem> agar UI tahu ID dan Label-nya
    suspend fun getSymptoms(): Result<List<SymptomItem>>
}