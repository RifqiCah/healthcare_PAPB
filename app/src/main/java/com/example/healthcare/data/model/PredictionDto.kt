package com.example.healthcare.data.model

import com.google.gson.annotations.SerializedName

// Data yang dikirim ke Flask
data class PredictionRequest(
    @SerializedName("gejala")
    val gejala: List<String>
)

// Data yang diterima dari Flask
data class PredictionResponse(
    @SerializedName("prediksi_penyakit")
    val namaPenyakit: String,

    @SerializedName("gejala_terdeteksi")
    val gejalaTerdeteksi: List<String>,

    @SerializedName("status")
    val status: String
)