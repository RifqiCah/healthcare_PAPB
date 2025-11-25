package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    // State Internal (Mutable)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)

    // State Eksternal (Read-Only)
    val authState: StateFlow<AuthState> = _authState

    /**
     * Fungsi setter publik untuk mengubah AuthState dari luar ViewModel.
     * Digunakan untuk menangani status seperti Idle atau Error dari Composable/Activity.
     */
    fun setAuthState(state: AuthState) {
        _authState.value = state
    }

    // LOGIN (Email/Password)
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
            }
            .addOnFailureListener { e ->
                val msg = when (e.message) {
                    null -> "Login gagal."
                    else -> when {
                        e.message!!.contains("user not found") ||
                                e.message!!.contains("no user") -> "Akun tidak ditemukan. Silakan register terlebih dahulu."
                        e.message!!.contains("password") -> "Password salah. Coba lagi."
                        else -> e.message!!
                    }
                }
                _authState.value = AuthState.Error(msg)
            }
    }

    // REGISTER (Email/Password)
    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
            }
            .addOnFailureListener {
                val msg = if (it.message?.contains("email address is already in use") == true) {
                    "Email ini sudah terdaftar. Silakan login atau gunakan email lain."
                } else {
                    it.message ?: "Register gagal."
                }
                _authState.value = AuthState.Error(msg)
            }
    }

    // SIGN IN DENGAN GOOGLE
    fun signInWithGoogle(credential: AuthCredential) {
        _authState.value = AuthState.Loading

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
            }
            .addOnFailureListener { e ->
                _authState.value = AuthState.Error(e.message ?: "Google Sign-In gagal")
            }
    }

    // RESET PASSWORD (Lupa Password)
    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Gagal mengirim email reset")
            }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}