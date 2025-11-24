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

data class ArticleUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val selectedCategory: String = "Semua",
    val error: String? = null,

    // TAMBAHAN BARU: Menyimpan token untuk halaman berikutnya
    val nextPageToken: String? = null
)

// ===============================================
// 2. ViewModel Implementation
// ===============================================

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArticleUiState())
    val uiState: StateFlow<ArticleUiState> = _uiState

    init {
        fetchArticles("Semua")
    }

    private fun mapCategoryToKeyword(categoryName: String): String {
        return when (categoryName) {
            "Gaya Hidup" -> "pola hidup OR tips sehat OR tidur"
            "Nutrisi" -> "gizi OR nutrisi OR makanan OR diet"
            "Olahraga" -> "olahraga OR senam OR lari OR gym"
            "Mental" -> "kesehatan mental OR stres OR depresi"
            else -> "health"
        }
    }

    /**
     * Fungsi utama fetch data.
     * @param isLoadMore: Jika true, berarti kita sedang mengambil halaman ke-2, ke-3, dst.
     */
    fun fetchArticles(categoryName: String, isLoadMore: Boolean = false) {
        val keyword = mapCategoryToKeyword(categoryName)

        // 1. Tentukan Token (Kalau load more ambil dari state, kalau awal null)
        val pageToken = if (isLoadMore) _uiState.value.nextPageToken else null

        // 2. Update State Loading
        _uiState.update { currentState ->
            // Kalau Load More, kita tidak perlu set isLoading=true (biar layar gak blank putih),
            // atau bisa bikin state khusus 'isLoadingMore'.
            // Di sini kita biarkan isLoading false jika load more agar UX lebih halus.
            if (isLoadMore) {
                currentState.copy(error = null)
            } else {
                // Kalau Refresh/Ganti Kategori, baru munculkan loading full screen
                currentState.copy(
                    isLoading = true,
                    error = null,
                    selectedCategory = categoryName,
                    articles = emptyList() // Kosongkan list dulu biar bersih
                )
            }
        }

        viewModelScope.launch {
            // Panggil Repository dengan pageToken
            repository.getArticlesByCategory(keyword, pageToken)
                .onSuccess { (newArticles, nextToken) ->
                    _uiState.update { current ->
                        current.copy(
                            // LOGIKA PENTING:
                            // Jika Load More -> Gabungkan list lama + list baru
                            // Jika Awal -> Pakai list baru saja
                            articles = if (isLoadMore) current.articles + newArticles else newArticles,

                            // Simpan token halaman berikutnya
                            nextPageToken = nextToken,

                            isLoading = false
                        )
                    }
                }
                .onFailure { exception ->
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
     * Fungsi ini dipanggil oleh UI ketika user scroll mentok bawah / klik tombol "Load More"
     */
    fun loadMore() {
        // Hanya jalan jika ada token halaman berikutnya
        if (_uiState.value.nextPageToken != null) {
            fetchArticles(_uiState.value.selectedCategory, isLoadMore = true)
        }
    }

    fun refresh() {
        // Refresh berarti ambil ulang dari halaman pertama (isLoadMore = false)
        fetchArticles(_uiState.value.selectedCategory, isLoadMore = false)
    }

    // --- FUNGSI DETAIL ---
    fun getArticleDetail(id: String): Article? {
        return repository.getArticleById(id)
    }
}