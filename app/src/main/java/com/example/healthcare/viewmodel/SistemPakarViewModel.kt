package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.domain.model.Diagnosa // <--- PENTING: Import Domain Model
import com.example.healthcare.domain.repository.SistemPakarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Status UI
sealed class PredictionState {
    object Idle : PredictionState()
    object Loading : PredictionState()

    // UBAH DISINI: Sekarang Success membawa objek 'Diagnosa' yang lengkap
    data class Success(val data: Diagnosa) : PredictionState()

    data class Error(val message: String) : PredictionState()
}

@HiltViewModel
class SistemPakarViewModel @Inject constructor(
    private val repository: SistemPakarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PredictionState>(PredictionState.Idle)
    val uiState: StateFlow<PredictionState> = _uiState

    fun predict(selectedGejala: List<String>) {
        if (selectedGejala.isEmpty()) {
            _uiState.value = PredictionState.Error("Pilih minimal satu gejala!")
            return
        }

        viewModelScope.launch {
            _uiState.value = PredictionState.Loading

            // Repository sekarang mengembalikan Result<Diagnosa>
            val result = repository.getPrediction(selectedGejala)

            result.onSuccess { diagnosa ->
                // Masukkan data domain 'diagnosa' ke dalam state Success
                _uiState.value = PredictionState.Success(diagnosa)
            }.onFailure { error ->
                _uiState.value = PredictionState.Error(error.message ?: "Gagal terhubung ke server")
            }
        }
    }

    // Fungsi untuk reset kalau mau ulang diagnosa
    fun resetState() {
        _uiState.value = PredictionState.Idle
    }
}