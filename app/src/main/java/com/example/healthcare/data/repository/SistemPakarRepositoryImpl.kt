package com.example.healthcare.data.repository

import com.example.healthcare.data.model.PredictionRequest
import com.example.healthcare.data.remote.SistemPakarApi
import com.example.healthcare.domain.model.Diagnosa // <--- Penting: Import Domain Model
import com.example.healthcare.domain.repository.SistemPakarRepository
import javax.inject.Inject

class SistemPakarRepositoryImpl @Inject constructor(
    private val api: SistemPakarApi
) : SistemPakarRepository {

    // Return type berubah jadi Result<Diagnosa>
    override suspend fun getPrediction(gejala: List<String>): Result<Diagnosa> {
        return try {
            // 1. Siapkan Request
            val request = PredictionRequest(gejala)

            // 2. Panggil API
            val response = api.predictDisease(request)

            // 3. Cek apakah sukses
            if (response.isSuccessful && response.body() != null) {
                val dataMentah = response.body()!!

                // --- PROSES MAPPING (DTO -> DOMAIN) ---
                // Kita pindahkan data dari 'dataMentah' ke wadah baru 'Diagnosa'
                val dataBersih = Diagnosa(
                    namaPenyakit = dataMentah.namaPenyakit,
                    gejala = dataMentah.gejalaTerdeteksi,
                    kepercayaan = dataMentah.status
                )

                // Kembalikan data yang sudah bersih
                Result.success(dataBersih)
            } else {
                Result.failure(Exception("Error Server: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e) // Error koneksi/internet
        }
    }
}