package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.data.model.SymptomItem // <-- Import Model Gejala
import com.example.healthcare.domain.model.Diagnosa
import com.example.healthcare.domain.repository.SistemPakarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// --- 1. STATE (UPDATE: Tambah list gejala dari API) ---
data class PredictionState(
    // Status Sistem
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasilDiagnosa: Diagnosa? = null,

    // Data Input User
    val umur: String = "",
    val gender: String = "",
    val selectedGejala: List<String> = emptyList(), // Menyimpan ID Gejala (Inggris)

    // DATA DARI API: Daftar Gejala (ID & Label Indo)
    val availableSymptoms: List<SymptomItem> = emptyList()
)

@HiltViewModel
class SistemPakarViewModel @Inject constructor(
    private val repository: SistemPakarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PredictionState())
    val uiState: StateFlow<PredictionState> = _uiState

    // Saat ViewModel dibuat, langsung minta daftar gejala ke API
    init {
        fetchSymptoms()
    }

    // --- FUNGSI AMBIL GEJALA DARI API ---
    private fun fetchSymptoms() {
        viewModelScope.launch {
            // Opsional: Set loading true jika ingin loading di awal banget
            // _uiState.update { it.copy(isLoading = true) }

            repository.getSymptoms()
                .onSuccess { symptoms ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            availableSymptoms = symptoms // Simpan ke State
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "Gagal memuat gejala: ${error.message}"
                        )
                    }
                }
        }
    }

    // --- INPUT LOGIC ---

    fun onUmurChange(newUmur: String) {
        if (newUmur.all { it.isDigit() }) {
            _uiState.update { it.copy(umur = newUmur) }
        }
    }

    fun onGenderChange(newGender: String) {
        _uiState.update { it.copy(gender = newGender) }
    }

    // Toggle Gejala (Simpan ID Inggrisnya)
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

    // --- LOGIKA PREDIKSI ---
    fun predict() {
        val currentState = _uiState.value

        if (currentState.umur.isEmpty() || currentState.gender.isEmpty()) {
            _uiState.update { it.copy(error = "Mohon isi umur dan jenis kelamin.") }
            return
        }
        if (currentState.selectedGejala.isEmpty()) {
            _uiState.update { it.copy(error = "Pilih minimal satu gejala!") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, hasilDiagnosa = null) }

            val result = repository.getPrediction(currentState.selectedGejala)

            result.onSuccess { diagnosa ->
                _uiState.update {
                    it.copy(isLoading = false, hasilDiagnosa = diagnosa)
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isLoading = false, error = error.message ?: "Gagal terhubung ke server")
                }
            }
        }
    }

    fun resetState() {
        // Reset inputan user, tapi JANGAN hapus availableSymptoms biar gak perlu fetch ulang
        _uiState.update { current ->
            current.copy(
                isLoading = false,
                error = null,
                hasilDiagnosa = null,
                umur = "",
                gender = "",
                selectedGejala = emptyList()
                // availableSymptoms tetap dipertahankan
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}