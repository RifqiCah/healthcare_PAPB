package com.example.healthcare.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// --- 1. STATE: Tambahkan photoUrl ---
data class ProfileUiState(
    val displayName: String = "Memuat...",
    val email: String = "Memuat...",
    val photoUrl: String? = null, // <-- TAMBAHAN BARU: Untuk menyimpan link foto
    val isLoading: Boolean = false,
    val passwordChangeMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance() // Init Storage

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadUserProfile()
    }

    // --- AMBIL DATA USER ---
    private fun loadUserProfile() {
        val user = auth.currentUser
        if (user != null) {
            // Ambil nama dari email jika displayName kosong
            val name = user.displayName.takeIf { !it.isNullOrBlank() }
                ?: user.email?.substringBefore("@")
                ?: "User"

            val email = user.email ?: "-"
            val photo = user.photoUrl?.toString() // Ambil URL foto dari Firebase Auth

            _uiState.update {
                it.copy(
                    displayName = name,
                    email = email,
                    photoUrl = photo, // Simpan ke state
                    isLoading = false
                )
            }
        }
    }

    // --- FUNGSI BARU: UPLOAD FOTO PROFIL ---
    fun updateProfilePicture(imageUri: Uri) {
        val user = auth.currentUser ?: return

        _uiState.update { it.copy(isLoading = true) }

        // 1. Buat referensi lokasi file di Firebase Storage
        // Format: profile_images/USER_ID.jpg
        val storageRef = storage.reference.child("profile_images/${user.uid}.jpg")

        // 2. Upload file
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                // 3. Jika sukses upload, ambil link download-nya (URL)
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    updateFirebaseAuthPhoto(downloadUri)
                }
            }
            .addOnFailureListener { e ->
                _uiState.update { it.copy(isLoading = false, passwordChangeMessage = "Gagal upload foto: ${e.message}") }
            }
    }

    // --- UPDATE DATA AUTH (Agar URL foto nempel di akun user) ---
    private fun updateFirebaseAuthPhoto(uri: Uri) {
        val user = auth.currentUser

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Update State Lokal biar UI langsung berubah
                    _uiState.update {
                        it.copy(
                            photoUrl = uri.toString(),
                            isLoading = false,
                            passwordChangeMessage = "Foto profil berhasil diperbarui!"
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
    }

    // ... (Fungsi changePassword dan clearMessage TETAP SAMA seperti sebelumnya) ...
    // Saya tulis ulang biar lengkap, copy paste saja semua ini.

    fun changePassword(oldPass: String, newPass: String) {
        val user = auth.currentUser
        val email = user?.email

        if (user != null && email != null) {
            _uiState.update { it.copy(isLoading = true) }
            val credential = EmailAuthProvider.getCredential(email, oldPass)

            user.reauthenticate(credential)
                .addOnSuccessListener {
                    user.updatePassword(newPass)
                        .addOnSuccessListener {
                            _uiState.update { it.copy(isLoading = false, passwordChangeMessage = "Sukses: Password berhasil diubah!") }
                        }
                        .addOnFailureListener { e ->
                            _uiState.update { it.copy(isLoading = false, passwordChangeMessage = "Gagal: ${e.message}") }
                        }
                }
                .addOnFailureListener {
                    _uiState.update { it.copy(isLoading = false, passwordChangeMessage = "Gagal: Password lama salah.") }
                }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(passwordChangeMessage = null) }
    }
}