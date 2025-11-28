package com.example.healthcare.data.repository

import com.example.healthcare.domain.model.DiagnosisHistory
import com.example.healthcare.domain.repository.SistemPakarRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SistemPakarRepositoryImpl @Inject constructor() : SistemPakarRepository {

    // Penyimpanan sementara di Memori (RAM)
    private val historyList = mutableListOf<DiagnosisHistory>()

    override suspend fun saveHistory(history: DiagnosisHistory) {
        historyList.add(0, history) // Simpan paling atas
    }

    override suspend fun getHistory(): List<DiagnosisHistory> {
        return historyList
    }

    // ✅ INI YANG TADI KURANG (Solusi Error-mu)
    override suspend fun clearHistory() {
        historyList.clear() // Hapus semua data di list
    }
}