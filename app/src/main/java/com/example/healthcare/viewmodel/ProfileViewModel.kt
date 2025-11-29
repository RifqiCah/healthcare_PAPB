package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// --- 1. STATE ---
data class ProfileUiState(
    val displayName: String = "Memuat...",
    val email: String = "Memuat...",
    val photoUrl: String? = null, // Tetap ada untuk MENAMPILKAN foto (misal dari Google)
    val isLoading: Boolean = false,
    val passwordChangeMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    // HAPUS: private val storage = FirebaseStorage... (Tidak lagi butuh Storage)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadUserProfile()
    }

    // --- AMBIL DATA USER ---
    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            // LOGIKA USERNAME:
            // 1. Cek 'displayName'. Ini berisi nama Google (jika login Google)
            //    ATAU username manual (jika saat Register kamu sudah set DisplayName).
            // 2. Jika kosong, ambil nama dari email (sebelum @).
            val name = if (!user.displayName.isNullOrBlank()) {
                user.displayName
            } else {
                user.email?.substringBefore("@") ?: "User"
            }

            val email = user.email ?: "-"
            val photo = user.photoUrl?.toString() // Ambil URL foto bawaan (misal dari Google)

            _uiState.update {
                it.copy(
                    displayName = name!!, // Pasti ada string karena logika di atas
                    email = email,
                    photoUrl = photo,
                    isLoading = false
                )
            }
        }
    }

    // HAPUS: fun updateProfilePicture(...)
    // HAPUS: fun updateFirebaseAuthPhoto(...)

    // --- GANTI PASSWORD (Tetap Diperlukan untuk User Manual) ---
    fun changePassword(oldPass: String, newPass: String) {
        val user = auth.currentUser
        val email = user?.email

        // Cek provider ID, user Google biasanya tidak bisa ganti password via method ini
        // Tapi untuk user manual (password), ini berfungsi.
        if (user != null && email != null) {
            _uiState.update { it.copy(isLoading = true) }

            // Re-autentikasi diperlukan sebelum ganti password sensitif
            val credential = EmailAuthProvider.getCredential(email, oldPass)

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    user.updatePassword(newPass)
                        .addOnSuccessListener {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    passwordChangeMessage = "Sukses: Password berhasil diubah!"
                                )
                            }
                        }
                        .addOnFailureListener { e ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    passwordChangeMessage = "Gagal ganti password: ${e.message}"
                                )
                            }
                        }
                }
                .addOnFailureListener {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            passwordChangeMessage = "Gagal: Password lama salah."
                        )
                    }
                }
        } else {
            _uiState.update {
                it.copy(passwordChangeMessage = "User tidak ditemukan atau login via provider lain.")
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(passwordChangeMessage = null) }
    }
}