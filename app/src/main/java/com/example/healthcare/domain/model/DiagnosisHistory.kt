package com.example.healthcare.domain.model

import java.util.Date

data class DiagnosisHistory(
    val id: String = "",
    val uid: String = "",          // ID User (Penting agar data tidak tertukar antar user)
    val penyakit: String = "",
    val persentase: Double = 0.0,
    val tanggal: String = "",      // String untuk tampilan UI (misal: "30 Nov 2025")
    val waktu: String = "",        // String untuk tampilan UI (misal: "14:00")
    val timestamp: Date? = null    // Object Date asli untuk sorting (mengurutkan) di backend
)