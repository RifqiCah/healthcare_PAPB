package com.example.healthcare.domain.model

// Wadah Utama
data class Diagnosa(
    // List kemungkinan (Top 1, Top 2, Top 3)
    val kemungkinan: List<KemungkinanPenyakit>
)

// Detail per Penyakit
data class KemungkinanPenyakit(
    val nama: String,
    val persentase: Int, // Kita bulatkan jadi Int biar gampang di UI (misal 98%)
    val deskripsi: String
)