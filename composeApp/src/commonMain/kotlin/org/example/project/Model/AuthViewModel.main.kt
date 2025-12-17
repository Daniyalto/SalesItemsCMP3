// FIL: Model/AuthViewModel.kt
package com.example.salesapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val currentUserEmail: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _state.update { it.copy(isLoggedIn = true, currentUserEmail = currentUser.email ?: "") }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(errorMessage = "Email and password cannot be empty") }
            return
        }

        _state.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // ÆGTE FIREBASE-KALD (suspend-funktion)
                auth.signInWithEmailAndPassword(email, password)

                val user = auth.currentUser
                _state.update { it.copy(isLoggedIn = true, currentUserEmail = user?.email ?: "", isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                // Håndter Firebase fejl
                _state.update { it.copy(errorMessage = e.message ?: "Login failed", isLoading = false) }
            }
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        if (password.length < 6) {
            _state.update { it.copy(errorMessage = "Password must be at least 6 characters") }
            return
        }
        _state.update { it.copy(isLoading = true, errorMessage = null) }



        viewModelScope.launch {
            try {
                // ÆGTE FIREBASE-KALD
                auth.createUserWithEmailAndPassword(email, password)

                val user = auth.currentUser
                _state.update { it.copy(isLoggedIn = true, currentUserEmail = user?.email ?: "", isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.message ?: "Registration failed", isLoading = false) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            auth.signOut()
            _state.update { AuthUiState() }
        }
    }
}

