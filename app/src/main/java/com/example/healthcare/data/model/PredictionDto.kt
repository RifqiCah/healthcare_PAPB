package com.example.healthcare.data.model

// --- MODEL DATA UNTUK VERSI OFFLINE (ONNX) ---

// 1. Untuk List Gejala (Checkbox UI)
// Menggantikan SymptomItem & SymptomResponse
data class SymptomData(
    val id: String,     // ID Inggris (cth: "headache") - Untuk input ke ONNX
    val label: String   // Label Indo (cth: "Sakit Kepala") - Untuk UI
)

// 2. Untuk Hasil Diagnosa
// Menggantikan DiagnosaItemDto & PredictionResponse
data class DiagnosaResult(
    val penyakit: String,
    val persentase: Float, // Pakai Float karena ONNX outputnya Float
    val deskripsi: String
)

// Catatan:
// Kita tidak butuh lagi 'PredictionRequest' atau 'SymptomResponse'
// karena kita tidak perlu parsing JSON "status": "success" dari server lagi.