package com.example.healthcare.domain.repository

import com.example.healthcare.domain.model.DiagnosisHistory


interface SistemPakarRepository {


    // ✅ PERTAHANKAN INI (Untuk History Lokal):
    suspend fun getHistory(): List<DiagnosisHistory>
    suspend fun saveHistory(history: DiagnosisHistory)
    suspend fun clearHistory()
}