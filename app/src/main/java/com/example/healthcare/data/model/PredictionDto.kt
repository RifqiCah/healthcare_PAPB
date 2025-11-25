package com.example.healthcare.data.model

import com.google.gson.annotations.SerializedName

// --- REQUEST ---
data class PredictionRequest(
    @SerializedName("gejala")
    val gejala: List<String>
)

// --- RESPONSE UTAMA ---
data class PredictionResponse(
    @SerializedName("status")
    val status: String,

    // UBAH INI: Sekarang menerima LIST, bukan String tunggal
    @SerializedName("diagnosa")
    val diagnosa: List<DiagnosaItemDto>,

    @SerializedName("gejala_input")
    val gejalaInput: List<String>
)

// --- ITEM DIAGNOSA (Penyakit, Persen, Deskripsi) ---
data class DiagnosaItemDto(
    @SerializedName("penyakit")
    val penyakit: String,

    @SerializedName("persentase")
    val persentase: Double,

    @SerializedName("deskripsi")
    val deskripsi: String?
)

// --- SYMPTOM RESPONSE (TETAP SAMA) ---
data class SymptomResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<SymptomItem>
)

data class SymptomItem(
    @SerializedName("id") val id: String,
    @SerializedName("label") val label: String
)