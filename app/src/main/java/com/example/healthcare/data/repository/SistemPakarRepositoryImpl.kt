package com.example.healthcare.data.repository

import com.example.healthcare.data.model.PredictionRequest
import com.example.healthcare.data.model.SymptomItem
import com.example.healthcare.data.remote.SistemPakarApi
import com.example.healthcare.domain.model.Diagnosa
import com.example.healthcare.domain.model.DiagnosisHistory
import com.example.healthcare.domain.model.KemungkinanPenyakit
import com.example.healthcare.domain.repository.SistemPakarRepository
import javax.inject.Inject

class SistemPakarRepositoryImpl @Inject constructor(
    private val api: SistemPakarApi
) : SistemPakarRepository {

    // --- PERBAIKAN: Masukkan list ke DALAM class ---
    // Karena Repo ini di-provide sebagai @Singleton oleh Hilt,
    // list ini akan tetap hidup selama aplikasi berjalan.
    private val historyList = mutableListOf<DiagnosisHistory>()

    override suspend fun saveHistory(history: DiagnosisHistory) {
        historyList.add(0, history) // Tambahkan ke paling atas (terbaru)
    }

    override suspend fun getHistory(): List<DiagnosisHistory> {
        return historyList
    }

    // --- FUNGSI PREDIKSI (SUDAH BENAR) ---
    override suspend fun getPrediction(gejala: List<String>): Result<Diagnosa> {
        return try {
            val request = PredictionRequest(gejala)
            val response = api.predictDisease(request)

            if (response.isSuccessful && response.body() != null) {
                val dataApi = response.body()!!

                val listKemungkinan = dataApi.diagnosa.map { item ->
                    KemungkinanPenyakit(
                        nama = item.penyakit,
                        persentase = item.persentase.toInt(),
                        deskripsi = item.deskripsi ?: "Tidak ada deskripsi tersedia."
                    )
                }

                Result.success(Diagnosa(kemungkinan = listKemungkinan))
            } else {
                Result.failure(Exception("Error Server: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- FUNGSI AMBIL GEJALA (SUDAH BENAR) ---
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