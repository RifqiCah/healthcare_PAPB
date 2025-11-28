package com.example.healthcare.domain.repository

import com.example.healthcare.domain.model.DiagnosisHistory
// Hapus import SymptomItem/Diagnosa dll yang lama

interface SistemPakarRepository {
    // ❌ HAPUS INI:
    // suspend fun getSymptoms(): Result<List<SymptomItem>>
    // suspend fun getPrediction(gejala: List<String>): Result<Diagnosa>

    // ✅ PERTAHANKAN INI (Untuk History Lokal):
    suspend fun getHistory(): List<DiagnosisHistory>
    suspend fun saveHistory(history: DiagnosisHistory)
    suspend fun clearHistory()
}