package com.example.healthcare.domain.repository

import com.example.healthcare.data.model.SymptomItem
import com.example.healthcare.domain.model.Diagnosa
import com.example.healthcare.domain.model.DiagnosisHistory // <-- Pastikan Import Model Baru ini

interface SistemPakarRepository {

    // Fungsi Prediksi (Lama)
    suspend fun getPrediction(gejala: List<String>): Result<Diagnosa>

    // Fungsi Ambil Gejala (Lama)
    suspend fun getSymptoms(): Result<List<SymptomItem>>

    // --- FUNGSI BARU (RIWAYAT) ---
    // Menyimpan satu data riwayat baru
    suspend fun saveHistory(history: DiagnosisHistory)

    // Mengambil semua daftar riwayat yang tersimpan
    suspend fun getHistory(): List<DiagnosisHistory>
}