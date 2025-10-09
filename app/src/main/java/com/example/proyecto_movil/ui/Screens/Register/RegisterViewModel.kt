package com.example.proyecto_movil.ui.Screens.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_movil.data.repository.AuthRepository
import com.example.proyecto_movil.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.proyecto_movil.ui.functions.Utils

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun updateNombrePersona(value: String) = _uiState.update { it.copy(nombrePersona = value) }
    fun updateNombreUsuario(value: String) = _uiState.update { it.copy(nombreUsuario = value) }
    fun updateEmail(value: String) = _uiState.update { it.copy(email = value) }
    fun updatePassword(value: String) = _uiState.update { it.copy(password = value) }
    fun toggleMostrarPassword() = _uiState.update { it.copy(mostrarPassword = !it.mostrarPassword) }
    fun toggleAcceptedTerms() = _uiState.update { it.copy(acceptedTerms = !it.acceptedTerms) }

    fun onRegisterClicked() {
        val s = _uiState.value

        if (!s.acceptedTerms) {
            showMessage("Debes aceptar los términos y condiciones")
            return
        }
        if (s.email.isBlank() || s.password.isBlank() || s.nombreUsuario.isBlank()) {
            showMessage("Completa email, contraseña y nombre de usuario")
            return
        }

        viewModelScope.launch {
            try {
                // 1) Crear la cuenta
                val result = auth.createUserWithEmailAndPassword(s.email.trim(), s.password).await()
                val uid = result.user?.uid ?: error("UID nulo tras registro")
                val userId = authRepository.currentUser?.uid ?: throw Exception("no se pudo obtener el usuario actual")
                userRepository.registerUser(
                    username = s.nombreUsuario,
                    email = s.email,
                    id = userId
                )
                val data = hashMapOf(
                    "id" to uid,
                    "email" to s.email.trim(),
                    "username" to s.nombreUsuario.trim(),
                    "name" to s.nombrePersona.trim(),
                    "followers" to 0,
                    "following" to 0,
                    "profileImageUrl" to "",
                    "createdAt" to FieldValue.serverTimestamp()
                )
                firestore.collection("users").document(uid)
                    .set(data, SetOptions.merge())
                    .await()

                // 3) Mensaje + navegación a Login
                _uiState.update {
                    it.copy(
                        showMessage = true,
                        errorMessage = "Cuenta creada. Inicia sesión.",
                        navigateToLogin = true
                    )
                }
            } catch (e: Exception) {
                showMessage(e.message ?: "Error al registrar")
            }
        }
    }

    fun onLoginClicked() {
        _uiState.update { it.copy(navigateToLogin = true) }
    }

    fun onBackClicked() {
        _uiState.update { it.copy(navigateBack = true) }
    }

    fun consumeNavigation() {
        _uiState.update {
            it.copy(
                navigateBack = false,
                navigateToLogin = false,
                navigateAfterRegister = false
            )
        }
    }

    fun consumeMessage() {
        _uiState.update { it.copy(showMessage = false) }
    }

    private fun showMessage(msg: String) {
        _uiState.update { it.copy(showMessage = true, errorMessage = msg) }
    }
}
