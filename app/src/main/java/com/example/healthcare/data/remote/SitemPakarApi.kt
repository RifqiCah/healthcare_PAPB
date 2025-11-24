package com.example.healthcare.data.remote


import com.example.healthcare.data.model.PredictionRequest
import com.example.healthcare.data.model.PredictionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SistemPakarApi {
    @POST("predict")
    suspend fun predictDisease(@Body request: PredictionRequest): Response<PredictionResponse>
}