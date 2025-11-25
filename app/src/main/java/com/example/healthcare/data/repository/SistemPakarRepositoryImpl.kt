package com.example.healthcare.data.repository

import com.example.healthcare.data.model.PredictionRequest
import com.example.healthcare.data.model.SymptomItem
import com.example.healthcare.data.remote.SistemPakarApi
import com.example.healthcare.domain.model.Diagnosa
import com.example.healthcare.domain.model.KemungkinanPenyakit
import com.example.healthcare.domain.repository.SistemPakarRepository
import javax.inject.Inject

class SistemPakarRepositoryImpl @Inject constructor(
    private val api: SistemPakarApi
) : SistemPakarRepository {

    // --- FUNGSI PREDIKSI (UPDATED) ---
    override suspend fun getPrediction(gejala: List<String>): Result<Diagnosa> {
        return try {
            val request = PredictionRequest(gejala)
            val response = api.predictDisease(request)

            if (response.isSuccessful && response.body() != null) {
                val dataApi = response.body()!!

                // --- MAPPING BARU (List DTO -> List Domain) ---
                val listKemungkinan = dataApi.diagnosa.map { item ->
                    KemungkinanPenyakit(
                        nama = item.penyakit,
                        persentase = item.persentase.toInt(), // Ubah Double ke Int
                        deskripsi = item.deskripsi ?: "Tidak ada deskripsi tersedia."
                    )
                }

                // Masukkan list ke dalam objek Diagnosa
                Result.success(Diagnosa(kemungkinan = listKemungkinan))
            } else {
                Result.failure(Exception("Error Server: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- FUNGSI AMBIL GEJALA (TETAP SAMA) ---
    override suspend fun getSymptoms(): Result<List<SymptomItem>> {
        return try {
            val response = api.getSymptoms()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.data)
            } else {
                Result.failure(Exception("Gagal mengambil data gejala"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}