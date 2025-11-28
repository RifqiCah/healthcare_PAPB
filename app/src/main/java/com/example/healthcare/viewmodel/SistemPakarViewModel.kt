package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.data.model.DiagnosaResult
import com.example.healthcare.data.model.SymptomData
import com.example.healthcare.domain.model.Article
import com.example.healthcare.domain.model.DiagnosisHistory
import com.example.healthcare.domain.repository.ArticleRepository
import com.example.healthcare.domain.repository.SistemPakarRepository
import com.example.healthcare.utils.LocalPakarBrain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// --- STATE DIPERBARUI ---
data class PredictionState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Perubahan Tipe Data ke Versi Baru
    val hasilDiagnosa: List<DiagnosaResult>? = null,
    val availableSymptoms: List<SymptomData> = emptyList(),

    val umur: String = "",
    val gender: String = "",
    val selectedGejala: List<String> = emptyList(), // Menyimpan ID gejala (Inggris)
    val relatedArticles: List<Article> = emptyList(),
    val isArticlesLoading: Boolean = false,
    val historyList: List<DiagnosisHistory> = emptyList()
)

@HiltViewModel
class SistemPakarViewModel @Inject constructor(
    private val pakarBrain: LocalPakarBrain,       // ✅ UTAMA: Otak AI Offline
    private val repository: SistemPakarRepository, // ✅ TETAP ADA: Untuk simpan History ke DB Lokal
    private val articleRepository: ArticleRepository // ✅ TETAP ADA: Untuk cari artikel berita
) : ViewModel() {

    private val _uiState = MutableStateFlow(PredictionState())
    val uiState: StateFlow<PredictionState> = _uiState

    init {
        loadSymptoms() // Load dari JSON Assets
        fetchHistory() // Load dari Room Database
    }

    // 1. Load Gejala (Offline dari JSON)
    private fun loadSymptoms() {
        viewModelScope.launch(Dispatchers.IO) {
            val symptoms = pakarBrain.getSymptomListForUi()
            _uiState.update {
                it.copy(availableSymptoms = symptoms)
            }
        }
    }

    // 2. Fetch History (Dari Database Lokal / Repository)
    fun fetchHistory() {
        viewModelScope.launch {
            // Asumsi: repository.getHistory() mengambil data dari Room Database, bukan API
            val history = repository.getHistory()
            _uiState.update { it.copy(historyList = history) }
        }
    }

    // --- INPUT HANDLERS ---
//    fun onUmurChange(newUmur: String) {
//        if (newUmur.all { it.isDigit() }) {
//            _uiState.update { it.copy(umur = newUmur) }
//        }
//    }
//
//    fun onGenderChange(newGender: String) {
//        _uiState.update { it.copy(gender = newGender) }
//    }

    fun toggleGejala(gejalaId: String) {
        _uiState.update { current ->
            val oldList = current.selectedGejala.toMutableList()
            if (oldList.contains(gejalaId)) {
                oldList.remove(gejalaId)
            } else {
                oldList.add(gejalaId)
            }
            current.copy(selectedGejala = oldList)
        }
    }

    // --- FUNGSI PREDIKSI (VERSI ONNX OFFLINE) ---
    fun predict() {
        val currentState = _uiState.value

        if (currentState.selectedGejala.isEmpty()) {
            _uiState.update { it.copy(error = "Pilih minimal satu gejala!") }
            return
        }

        // Jalankan di Background Thread (IO)
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null, hasilDiagnosa = null) }

            // 🔥 LOGIKA BARU: Panggil Otak Lokal
            val results = pakarBrain.predict(currentState.selectedGejala)

            if (results.isNotEmpty()) {
                _uiState.update {
                    it.copy(isLoading = false, hasilDiagnosa = results)
                }
                // Cari artikel berdasarkan penyakit teratas (Top 1)
                fetchRelatedArticles(results)
            } else {
                _uiState.update {
                    it.copy(isLoading = false, error = "Gejala tidak cukup spesifik atau tidak dikenali.")
                }
            }
        }
    }

    // Cari Artikel Berita (News API)
    private fun fetchRelatedArticles(diagnosaList: List<DiagnosaResult>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isArticlesLoading = true) }

            // Ambil 3 penyakit teratas untuk keyword pencarian
            val topDiseases = diagnosaList.take(3).map { it.penyakit }
            val queryKeyword = topDiseases.joinToString(" OR ")

            val result = articleRepository.getArticlesByCategory(queryKeyword, null)

            result.onSuccess { (articles, _) ->
                _uiState.update { it.copy(isArticlesLoading = false, relatedArticles = articles) }
            }.onFailure {
                _uiState.update { it.copy(isArticlesLoading = false) }
            }
        }
    }

    fun resetState() {
        _uiState.update { current ->
            current.copy(
                isLoading = false,
                error = null,
                hasilDiagnosa = null,
                umur = "",
                gender = "",
                selectedGejala = emptyList(),
                relatedArticles = emptyList()
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearDiagnosa() {
        _uiState.update { it.copy(hasilDiagnosa = null, error = null) }
    }

    // Simpan ke History (Database Lokal)
    fun saveResultToHistory() {
        // Ambil Top 1 Hasil Diagnosa
        val topResult = _uiState.value.hasilDiagnosa?.firstOrNull() ?: return

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val now = Date()

        val newHistory = DiagnosisHistory(
            id = System.currentTimeMillis().toString(),
            penyakit = topResult.penyakit,
            persentase = topResult.persentase.toDouble(), // Convert Float ke Double sesuai Model History lama
            tanggal = dateFormat.format(now),
            waktu = timeFormat.format(now)
        )

        viewModelScope.launch {
            repository.saveHistory(newHistory)
            fetchHistory() // Refresh list history
            resetState()   // Reset tampilan diagnosa
        }
    }
}