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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

// --- STATE UI ---
data class PredictionState(
    val isLoading: Boolean = false,
    val error: String? = null,

    // Data untuk Diagnosa
    val hasilDiagnosa: List<DiagnosaResult>? = null,
    val availableSymptoms: List<SymptomData> = emptyList(),
    val selectedGejala: List<String> = emptyList(), // ID Gejala yang dipilih

    // Data Pendukung
    val umur: String = "",
    val gender: String = "",

    // Data Artikel & Riwayat
    val relatedArticles: List<Article> = emptyList(),
    val isArticlesLoading: Boolean = false,
    val historyList: List<DiagnosisHistory> = emptyList() // List Riwayat
)

@HiltViewModel
class SistemPakarViewModel @Inject constructor(
    private val pakarBrain: LocalPakarBrain,       // Otak AI (Offline)
    private val repository: SistemPakarRepository, // Repository DB (History)
    private val articleRepository: ArticleRepository // Repository Artikel
) : ViewModel() {

    private val _uiState = MutableStateFlow(PredictionState())
    val uiState: StateFlow<PredictionState> = _uiState

    init {
        loadSymptoms() // Muat gejala saat aplikasi mulai
        fetchHistory() // Muat riwayat agar Home Screen & Sistem Pakar Screen tahu statusnya
    }

    // --- 1. LOAD DATA AWAL ---
    private fun loadSymptoms() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val symptoms = pakarBrain.getSymptomListForUi()
                _uiState.update { it.copy(availableSymptoms = symptoms) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Gagal memuat gejala: ${e.message}") }
            }
        }
    }

    // --- 2. MANAJEMEN RIWAYAT (CRUD) ---

    // A. Ambil Data (DIPERBAIKI: Handle Loading State agar tidak flickering)
    fun fetchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            // Hanya set loading jika list masih kosong (biar tidak loading ulang saat delete)
            if (_uiState.value.historyList.isEmpty()) {
                _uiState.update { it.copy(isLoading = true) }
            }

            try {
                val history = repository.getHistory()
                _uiState.update { it.copy(historyList = history, isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false) } // Pastikan loading mati walau error
            }
        }
    }

    // B. Simpan Data (Aman dengan NonCancellable)
    fun saveResultToHistory() {
        // Ambil hasil diagnosa tertinggi (Rank 1)
        val currentState = _uiState.value
        val topResult = currentState.hasilDiagnosa?.firstOrNull() ?: return

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val now = Date()

        // Mapping dari Hasil AI ke Model Database
        val newHistory = DiagnosisHistory(
            id = System.currentTimeMillis().toString(), // ID Unik berdasarkan waktu
            penyakit = topResult.penyakit,
            persentase = topResult.persentase.toDouble(),
            tanggal = dateFormat.format(now),
            waktu = timeFormat.format(now)
            // uid & timestamp akan diisi otomatis di RepositoryImpl
        )

        viewModelScope.launch(Dispatchers.IO) {
            // Menggunakan NonCancellable agar proses simpan tidak mati saat user pindah layar
            withContext(NonCancellable) {
                try {
                    repository.saveHistory(newHistory)
                    // Tidak perlu fetchHistory() di sini karena ViewModel akan dihancurkan.
                    // Fetch ulang dilakukan saat onResume di screen dashboard.
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // C. Hapus Data (DENGAN OPTIMISTIC UPDATE)
    fun deleteHistoryItem(historyId: String) {
        // 1. Simpan backup list jika perlu restore
        val oldList = _uiState.value.historyList

        // 2. HAPUS DULU DI UI (Instan)
        val updatedList = oldList.filter { it.id != historyId }
        _uiState.update { it.copy(historyList = updatedList) }

        // 3. BARU HAPUS DI DATABASE (Di Background)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteHistory(historyId)
                // Sukses hapus, tidak perlu fetch ulang agar hemat data
            } catch (e: Exception) {
                // Gagal hapus? Kembalikan data lama (Rollback)
                _uiState.update {
                    it.copy(
                        historyList = oldList, // Kembalikan list awal
                        error = "Gagal menghapus: ${e.message}"
                    )
                }
            }
        }
    }

    // --- 3. LOGIKA DIAGNOSA ---
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

    fun predict() {
        val currentState = _uiState.value

        if (currentState.selectedGejala.isEmpty()) {
            _uiState.update { it.copy(error = "Pilih minimal satu gejala!") }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, error = null, hasilDiagnosa = null) }

            try {
                // Panggil AI Local
                val results = pakarBrain.predict(currentState.selectedGejala)

                if (results.isNotEmpty()) {
                    _uiState.update {
                        it.copy(isLoading = false, hasilDiagnosa = results)
                    }
                    // Cari artikel berdasarkan hasil diagnosa
                    fetchRelatedArticles(results)
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, error = "Penyakit tidak teridentifikasi berdasarkan gejala ini.")
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Terjadi kesalahan: ${e.message}") }
            }
        }
    }

    // --- 4. ARTIKEL TERKAIT ---
    private fun fetchRelatedArticles(diagnosaList: List<DiagnosaResult>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isArticlesLoading = true) }

            // Ambil nama penyakit top 3 untuk keyword pencarian
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

    // --- 5. HELPER FUNCTIONS ---
    fun resetState() {
        _uiState.update { current ->
            current.copy(
                isLoading = false,
                error = null,
                hasilDiagnosa = null,
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
}