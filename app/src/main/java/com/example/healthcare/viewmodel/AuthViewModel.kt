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

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Login failed")
            }
    }

    fun register(email: String, password: String) {
        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _authState.value = AuthState.Success
            }
            .addOnFailureListener {
                _authState.value = AuthState.Error(it.message ?: "Register failed")
            }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}