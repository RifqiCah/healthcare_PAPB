package com.example.healthcare.data.remote

import com.example.healthcare.data.model.PredictionRequest
import com.example.healthcare.data.model.PredictionResponse
import com.example.healthcare.data.model.SymptomResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SistemPakarApi {
    // Endpoint Prediksi
    @POST("predict")
    suspend fun predictDisease(@Body request: PredictionRequest): Response<PredictionResponse>

    // Endpoint Ambil Daftar Gejala (BARU)
    @GET("symptoms")
    suspend fun getSymptoms(): Response<SymptomResponse>
}