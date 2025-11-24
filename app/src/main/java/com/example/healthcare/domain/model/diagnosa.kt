package com.example.healthcare.domain.model

// Model ini bersih, khusus buat UI. Gak peduli datanya dari Flask atau Firebase.
data class Diagnosa(
    val namaPenyakit: String,
    val gejala: List<String>,
    val kepercayaan: String // alias status
)