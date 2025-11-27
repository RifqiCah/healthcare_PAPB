package com.example.healthcare.domain.model

data class DiagnosisHistory(
    val id: String,
    val penyakit: String,
    val persentase: Int,
    val tanggal: String,
    val waktu: String
)