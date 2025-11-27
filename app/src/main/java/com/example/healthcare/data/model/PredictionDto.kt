package com.example.healthcare.data.model

import com.google.gson.annotations.SerializedName

// --- RESPONSE UTAMA ---
// Cocok dengan: jsonify({'status': 'success', 'data': ...})
data class SymptomResponse(
    @SerializedName("status")
    val status: String? = null, // Kita kasih ? (Nullable) biar kalau server lupa kirim status, app tidak crash

    @SerializedName("data")
    val data: List<SymptomItem> = emptyList() // Default list kosong jika data null
)

data class SymptomItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("label")
    val label: String
)


// --- REQUEST (Data yang dikirim ke API) ---
data class PredictionRequest(
    @SerializedName("gejala")
    val gejala: List<String>
)

// --- RESPONSE UTAMA (Data yang diterima dari API) ---
data class PredictionResponse(
    @SerializedName("status")
    val status: String? = null,

    // INI YANG HILANG SEBELUMNYA:
    @SerializedName("diagnosa")
    val diagnosa: List<DiagnosaItemDto> = emptyList(), // Biar repo bisa panggil .diagnosa

    @SerializedName("gejala_input")
    val gejalaInput: List<String> = emptyList()
)

// --- ITEM DETAIL (Data detail per penyakit) ---
data class DiagnosaItemDto(
    // INI YANG HILANG SEBELUMNYA:
    @SerializedName("penyakit")
    val penyakit: String,      // Biar repo bisa panggil .penyakit

    @SerializedName("persentase")
    val persentase: Double,    // Biar repo bisa panggil .persentase

    @SerializedName("deskripsi")
    val deskripsi: String?     // Biar repo bisa panggil .deskripsi
)