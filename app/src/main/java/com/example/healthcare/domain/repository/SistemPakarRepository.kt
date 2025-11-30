package com.example.healthcare.domain.repository

import com.example.healthcare.domain.model.DiagnosisHistory

interface SistemPakarRepository {

    // Ambil semua riwayat user
    suspend fun getHistory(): List<DiagnosisHistory>

    // Simpan hasil diagnosa baru
    suspend fun saveHistory(history: DiagnosisHistory)

    // Hapus satu item riwayat berdasarkan ID (Fitur Delete Icon)
    suspend fun deleteHistory(historyId: String)
}