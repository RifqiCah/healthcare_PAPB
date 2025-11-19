package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcare.domain.model.Article
import com.example.healthcare.domain.repository.ArticleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject // Diperlukan jika Anda menggunakan Hilt atau Koin

// ===============================================
// 1. UI State Definition
// ===============================================

/**
 * Merepresentasikan state UI saat ini untuk ArtikelScreen.
 * Semua yang ingin ditampilkan di UI harus ada di sini.
 */
data class ArticleUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCategory: String = "Semua", // Kategori yang sedang dipilih
    val error: String? = null
)

// ===============================================
// 2. ViewModel Implementation
// ===============================================

class ArticleViewModel @Inject constructor(
    // ArticleRepository di-inject; ViewModel hanya berinteraksi dengan kontrak ini.
    private val repository: ArticleRepository
) : ViewModel() {

    // MutableStateFlow untuk menyimpan state yang dapat diubah di dalam ViewModel
    private val _uiState = MutableStateFlow(ArticleUiState())

    // StateFlow yang akan diekspos ke Composable/UI (hanya read-only)
    val uiState: StateFlow<ArticleUiState> = _uiState

    // Inisialisasi: Ambil data saat ViewModel pertama kali dibuat
    init {
        // Fetch artikel default saat aplikasi pertama kali masuk ke layar artikel
        fetchArticles(categoryName = "Semua")
    }

    /**
     * Memetakan nama kategori UI ke kata kunci (query) yang akan dikirim ke News API.
     */
    private fun mapCategoryToKeyword(categoryName: String): String {
        return when (categoryName) {
            "Gaya Hidup" -> "lifestyle AND (health OR wellness)"
            "Nutrisi" -> "nutrition OR diet OR supplements"
            "Olahraga" -> "exercise OR fitness OR workout"
            else -> "health OR medical OR wellness" // "Semua"
        }
    }

    /**
     * Fungsi publik yang dipanggil oleh UI saat kategori diubah.
     */
    fun fetchArticles(categoryName: String) {
        // Ambil kata kunci API berdasarkan nama kategori UI
        val keyword = mapCategoryToKeyword(categoryName)

        // Update state: Set isLoading ke true dan clear error lama
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                selectedCategory = categoryName // Simpan kategori yang dipilih
            )
        }

        // Jalankan operasi suspend di coroutine scope milik ViewModel
        viewModelScope.launch {
            repository.getArticlesByCategory(keyword)
                .onSuccess { articles ->
                    // Update state saat pengambilan data berhasil
                    _uiState.update {
                        it.copy(
                            articles = articles,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    // Update state saat terjadi kegagalan (jaringan/parsing)
                    _uiState.update {
                        it.copy(
                            error = error.message ?: "Gagal memuat artikel.",
                            isLoading = false
                        )
                    }
                }
        }
    }
}