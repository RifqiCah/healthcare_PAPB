package com.example.healthcare.utils

import android.content.Context
import android.util.Log
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import ai.onnxruntime.OnnxTensor
import com.example.healthcare.data.model.DiagnosaResult
import com.example.healthcare.data.model.SymptomData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.FloatBuffer
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalPakarBrain @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var ortEnv: OrtEnvironment? = null
    private var ortSession: OrtSession? = null
    private val gson = Gson()

    // Variable Data
    private var symptomNamesList: List<String> = emptyList()
    private var symptomMapIndo: Map<String, String> = emptyMap()
    private var diseaseIdMap: Map<String, String> = emptyMap()
    private var diseaseIndoMap: Map<String, String> = emptyMap()
    private var diseaseDescriptions: Map<String, String> = emptyMap()

    init {
        setupAI()
    }

    private fun setupAI() {
        Log.d("PAKAR_BRAIN", "🔥 [START] MEMULAI AI...")
        try {
            ortEnv = OrtEnvironment.getEnvironment()

            val assetsFiles = context.assets.list("")?.toList() ?: emptyList()
            if (assetsFiles.contains("model_sistempakar.onnx")) {
                val modelBytes = context.assets.open("model_sistempakar.onnx").readBytes()
                ortSession = ortEnv?.createSession(modelBytes)
                Log.d("PAKAR_BRAIN", "✅ Model Loaded")
            } else {
                Log.e("PAKAR_BRAIN", "❌ MODEL TIDAK DITEMUKAN")
            }

            loadAllJsonData(assetsFiles)

        } catch (e: Exception) {
            Log.e("PAKAR_BRAIN", "❌ ERROR SETUP: ${e.message}")
        }
    }

    private fun loadAllJsonData(assetsFiles: List<String>) {
        try {
            // 1. SYMPTOM NAMES (DAFTAR GEJALA)
            val symptomFile = if (assetsFiles.contains("symptom_names.json")) "symptom_names.json" else "symptom_mapping.json"

            val namesJson = context.assets.open(symptomFile).bufferedReader().use { it.readText() }
            val rawList: List<String> = gson.fromJson(namesJson, object : TypeToken<List<String>>() {}.type)

            // Paksa Lowercase & Trim agar pencarian tidak error
            symptomNamesList = rawList.map { it.trim().lowercase() }
            Log.d("PAKAR_BRAIN", "📋 Loaded ${symptomNamesList.size} gejala.")

            // 2. MAPPING INDO
            if (assetsFiles.contains("symptom_map_indo.json")) {
                val mapJson = context.assets.open("symptom_map_indo.json").bufferedReader().use { it.readText() }
                val rawMap: Map<String, String> = gson.fromJson(mapJson, object : TypeToken<Map<String, String>>() {}.type)
                symptomMapIndo = rawMap.mapKeys { it.key.trim().lowercase() }
            }

            // 3. DISEASE MAP (ID -> NAME INGGRIS)
            val diseaseMapFile = if (assetsFiles.contains("disease_mapping_baru.json")) "disease_mapping_baru.json" else "disease_mapping_indo.json"
            val diseaseJson = context.assets.open(diseaseMapFile).bufferedReader().use { it.readText() }
            diseaseIdMap = gson.fromJson(diseaseJson, object : TypeToken<Map<String, String>>() {}.type)

            // 4. DESCRIPTIONS
            if (assetsFiles.contains("disease_descriptions.json")) {
                val descJson = context.assets.open("disease_descriptions.json").bufferedReader().use { it.readText() }
                diseaseDescriptions = gson.fromJson(descJson, object : TypeToken<Map<String, String>>() {}.type)
            }

            // 5. INDO DISEASE MAP (INGGRIS -> INDO)
            if (assetsFiles.contains("disease_mapping_indo.json")) {
                val indoJson = context.assets.open("disease_mapping_indo.json").bufferedReader().use { it.readText() }
                diseaseIndoMap = gson.fromJson(indoJson, object : TypeToken<Map<String, String>>() {}.type)
            }

        } catch (e: Exception) {
            Log.e("PAKAR_BRAIN", "❌ LOAD JSON ERROR: ${e.message}")
        }
    }

    fun getSymptomListForUi(): List<SymptomData> {
        val list = mutableListOf<SymptomData>()
        for (engName in symptomNamesList) {
            // engName sudah lowercase & trim dari loadAllJsonData
            val indoName = symptomMapIndo[engName] ?: engName.replace("_", " ").capitalizeWords()
            list.add(SymptomData(id = engName, label = indoName))
        }
        return list
    }

    // --- FUNGSI PREDIKSI FINAL (SUDAH FIX 2 DESIMAL & ARRAY) ---
    fun predict(userSymptomsEng: List<String>): List<DiagnosaResult> {
        if (ortSession == null) return emptyList()

        try {
            Log.d("PAKAR_BRAIN", "🔍 Menganalisa input UI: $userSymptomsEng")

            if (userSymptomsEng.isEmpty()) return emptyList()

            // 1. One-Hot Encoding
            val inputVector = FloatArray(symptomNamesList.size) { 0f }
            var matchCount = 0

            for (symptom in userSymptomsEng) {
                val cleanSymptom = symptom.trim().lowercase()
                val index = symptomNamesList.indexOf(cleanSymptom)

                if (index != -1) {
                    inputVector[index] = 1f
                    matchCount++
                } else {
                    Log.w("PAKAR_BRAIN", "⚠️ Gejala tidak ditemukan di model: $cleanSymptom")
                }
            }

            if (matchCount == 0) {
                Log.e("PAKAR_BRAIN", "❌ TOTAL MATCH: 0. PREDIKSI BATAL.")
                return emptyList()
            }

            // 2. Run ONNX
            val inputName = ortSession!!.inputNames.iterator().next()
            val shape = longArrayOf(1, symptomNamesList.size.toLong())
            val tensor = OnnxTensor.createTensor(ortEnv, FloatBuffer.wrap(inputVector), shape)

            val result = ortSession!!.run(Collections.singletonMap(inputName, tensor))

            // --- AMBIL DATA ARRAY (FIX float[][]) ---
            val rawOutput = result.get(1).value
            val probabilitiesArray = rawOutput as Array<FloatArray>
            val firstRow = probabilitiesArray[0] // Ambil baris pertama

            // 3. Format Hasil & Pembulatan
            // Kita gabungkan index dengan skornya dulu
            val results = firstRow.mapIndexed { index, score ->
                index to score // Pair(Index, Score)
            }
                .sortedByDescending { it.second } // Urutkan berdasarkan Skor (second)
                .take(3) // Ambil 3 besar
                .map { (id, score) ->
                    val idString = id.toString()

                    // Cari Nama Penyakit
                    val engDiseaseName = diseaseIdMap[idString] ?: "Unknown ($idString)"
                    val indoName = diseaseIndoMap[idString] ?: diseaseIndoMap[engDiseaseName] ?: engDiseaseName

                    val description = diseaseDescriptions[engDiseaseName]
                        ?: diseaseDescriptions[indoName]
                        ?: "Deskripsi belum tersedia."

                    // 🔥 LOGIKA PEMBULATAN 2 DESIMAL 🔥
                    // Skor asli: 0.97543...
                    // Dikali 10000 -> 9754.3... -> Dibulatkan -> 9754 -> Dibagi 100f -> 97.54
                    val roundedPercent = kotlin.math.round(score * 10000) / 100f

                    DiagnosaResult(
                        penyakit = indoName,
                        persentase = roundedPercent,
                        deskripsi = description
                    )
                }

            Log.d("PAKAR_BRAIN", "✅ HASIL TOP 1: ${results.firstOrNull()?.penyakit} (${results.firstOrNull()?.persentase}%)")
            return results

        } catch (e: Exception) {
            Log.e("PAKAR_BRAIN", "❌ PREDICT ERROR: ${e.message}")
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}