package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.domain.model.Article
import com.example.healthcare.domain.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ===============================================
// 1. UI State Definition
// ===============================================

/**
 * State UI untuk ArticleScreen.
 * Data class ini menyimpan semua data yang dibutuhkan tampilan.
 */
data class ArticleUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCategory: String = "Semua", // Untuk memantau tab mana yang aktif
    val error: String? = null
)

// ===============================================
// 2. ViewModel Implementation
// ===============================================

@HiltViewModel // <--- WAJIB ADA: Memberitahu Hilt bahwa ini adalah ViewModel
class ArticleViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    // Backing property (bisa diubah di dalam ViewModel)
    private val _uiState = MutableStateFlow(ArticleUiState())

    // Exposed property (hanya bisa dibaca oleh UI)
    val uiState: StateFlow<ArticleUiState> = _uiState

    // Init block: Jalan otomatis saat ViewModel pertama kali dibuat
    init {
        fetchArticles("Semua")
    }

    /**
     * Mengubah Nama Kategori UI (Bahasa Indonesia) menjadi Query API (Bahasa Inggris).
     * Contoh: "Olahraga" -> "health AND (exercise OR fitness)"
     */
    private fun mapCategoryToKeyword(categoryName: String): String {
        return when (categoryName) {
            "Gaya Hidup" -> "lifestyle AND (health OR wellness)"
            "Nutrisi" -> "nutrition OR diet OR supplements"
            "Olahraga" -> "exercise OR fitness OR workout"
            "Mental" -> "mental health OR psychology OR stress"
            else -> "health" // Default query untuk "Semua"
        }
    }

    /**
     * Fungsi utama untuk mengambil data.
     * Dipanggil saat init atau saat user klik tombol Kategori.
     */
    fun fetchArticles(categoryName: String) {
        val keyword = mapCategoryToKeyword(categoryName)

        // 1. Update State: Loading = true, Hapus Error lama
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true,
                error = null,
                selectedCategory = categoryName
            )
        }

        // 2. Panggil Repository di Background Thread
        viewModelScope.launch {
            repository.getArticlesByCategory(keyword)
                .onSuccess { articlesList ->
                    // Berhasil: Masukkan data ke state, matikan loading
                    _uiState.update {
                        it.copy(
                            articles = articlesList,
                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
                    // Gagal: Simpan pesan error, matikan loading
                    _uiState.update {
                        it.copy(
                            error = exception.message ?: "Gagal memuat data",
                            isLoading = false
                        )
                    }
                }
        }
    }

    /**
     * Opsional: Fungsi untuk me-refresh data (misal untuk fitur SwipeRefresh)
     */
    fun refresh() {
        // Ambil ulang data berdasarkan kategori yang sedang dipilih saat ini
        fetchArticles(_uiState.value.selectedCategory)
    }
}