package com.example.healthcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // LOGIN
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val user = auth.currentUser
                if (user != null && !user.isEmailVerified) {
                    _authState.value =
                        AuthState.Error("Email belum diverifikasi. Silakan cek email Anda.")
                    return@addOnSuccessListener
                }

                _authState.value = AuthState.Success
            }
            .addOnFailureListener { e ->
                val msg = when (e.message) {
                    null -> "Login gagal."
                    else -> when {
                        e.message!!.contains("user record") ||
                                e.message!!.contains("no user") -> "Akun tidak ditemukan. Silakan register terlebih dahulu."
                        e.message!!.contains("password") -> "Password salah."
                        else -> e.message!!
                    }
                }
                _authState.value = AuthState.Error(msg)
            }
    }

    // REGISTER
    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = auth.currentUser
                user?.sendEmailVerification()

                _authState.value =
                    AuthState.Error("Akun berhasil dibuat. Cek email Anda untuk verifikasi.")
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Register gagal")
            }
    }

    // RESET PASSWORD
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