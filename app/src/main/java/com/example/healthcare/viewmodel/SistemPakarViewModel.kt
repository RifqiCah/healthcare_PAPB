package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.data.model.SymptomItem
import com.example.healthcare.domain.model.Article
import com.example.healthcare.domain.model.Diagnosa
import com.example.healthcare.domain.repository.ArticleRepository
import com.example.healthcare.domain.repository.SistemPakarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Import Date Format
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.healthcare.domain.model.DiagnosisHistory

// --- STATE ---
data class PredictionState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasilDiagnosa: Diagnosa? = null,
    val umur: String = "",
    val gender: String = "",
    val selectedGejala: List<String> = emptyList(),
    val availableSymptoms: List<SymptomItem> = emptyList(),
    val relatedArticles: List<Article> = emptyList(),
    val isArticlesLoading: Boolean = false,
    val historyList: List<DiagnosisHistory> = emptyList()
)

@HiltViewModel
class SistemPakarViewModel @Inject constructor(
    private val repository: SistemPakarRepository,
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PredictionState())
    val uiState: StateFlow<PredictionState> = _uiState

    init {
        fetchSymptoms()
        fetchHistory()
    }

    // Fungsi fetchHistory (Public agar bisa dipanggil dari UI untuk Auto-Refresh)
    fun fetchHistory() {
        viewModelScope.launch {
            val history = repository.getHistory()
            _uiState.update { it.copy(historyList = history) }
        }
    }

    private fun fetchSymptoms() {
        viewModelScope.launch {
            repository.getSymptoms()
                .onSuccess { symptoms ->
                    _uiState.update { it.copy(isLoading = false, availableSymptoms = symptoms) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = "Gagal memuat gejala: ${error.message}") }
                }
        }
    }

    // Input handlers (Tetap dibiarkan ada, tidak masalah meskipun tidak dipakai di UI)
    fun onUmurChange(newUmur: String) {
        if (newUmur.all { it.isDigit() }) {
            _uiState.update { it.copy(umur = newUmur) }
        }
    }

    fun onGenderChange(newGender: String) {
        _uiState.update { it.copy(gender = newGender) }
    }

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

    // --- FUNGSI PREDIKSI (SUDAH DIPERBAIKI) ---
    fun predict() {
        val currentState = _uiState.value

        // VALIDASI UMUR & GENDER SUDAH DIHAPUS
        // Sekarang hanya cek apakah gejala sudah dipilih
        if (currentState.selectedGejala.isEmpty()) {
            _uiState.update { it.copy(error = "Pilih minimal satu gejala!") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, hasilDiagnosa = null) }

            val result = repository.getPrediction(currentState.selectedGejala)

            result.onSuccess { diagnosa ->
                _uiState.update { it.copy(isLoading = false, hasilDiagnosa = diagnosa) }
                fetchRelatedArticles(diagnosa)
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, error = error.message ?: "Gagal terhubung ke server") }
            }
        }
    }

    private fun fetchRelatedArticles(diagnosa: Diagnosa) {
        viewModelScope.launch {
            _uiState.update { it.copy(isArticlesLoading = true) }
            val topDiseases = diagnosa.kemungkinan.take(3).map { it.nama }
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

    fun saveResultToHistory() {
        val currentResult = _uiState.value.hasilDiagnosa?.kemungkinan?.firstOrNull() ?: return

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val now = Date()

        val newHistory = DiagnosisHistory(
            id = System.currentTimeMillis().toString(),
            penyakit = currentResult.nama,
            persentase = currentResult.persentase,
            tanggal = dateFormat.format(now),
            waktu = timeFormat.format(now)
        )

        viewModelScope.launch {
            repository.saveHistory(newHistory)
            fetchHistory()
            resetState()
        }
    }
}