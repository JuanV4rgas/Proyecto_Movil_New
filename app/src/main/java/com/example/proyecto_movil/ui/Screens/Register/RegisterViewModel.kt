package com.example.proyecto_movil.ui.Screens.Register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyecto_movil.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    fun updateNombrePersona(v: String) = _uiState.update { it.copy(nombrePersona = v) }
    fun updateNombreUsuario(v: String) = _uiState.update { it.copy(nombreUsuario = v) }
    fun updateEmail(v: String) = _uiState.update { it.copy(email = v) }
    fun updatePassword(v: String) = _uiState.update { it.copy(password = v) }
    fun toggleMostrarPassword() = _uiState.update { it.copy(mostrarPassword = !it.mostrarPassword) }
    fun toggleAcceptedTerms() = _uiState.update { it.copy(acceptedTerms = !it.acceptedTerms) }

    fun onBackClicked() = _uiState.update { it.copy(navigateBack = true) }
    fun onLoginClicked() = _uiState.update { it.copy(navigateToLogin = true) }

    fun consumeMessage() = _uiState.update { it.copy(showMessage = false, errorMessage = null) }
    fun consumeNavigation() = _uiState.update {
        it.copy(navigateBack = false, navigateToLogin = false, navigateAfterRegister = false)
    }

    fun onRegisterClicked() {
        val s = _uiState.value
        if (!s.acceptedTerms) {
            _uiState.update { it.copy(showMessage = true, errorMessage = "Debes aceptar los términos") }
            return
        }
        viewModelScope.launch {
            val uid = s.email // DEMO: usa el email como id; en real, usa FirebaseAuth.uid
            userRepository.registerUser(
                id = uid,
                username = s.nombreUsuario,
                email = s.email,
                bio = s.nombrePersona,
                profilePic = "" // ← no null
            ).fold(
                onSuccess = { _uiState.update { it.copy(navigateAfterRegister = true) } },
                onFailure = { e ->
                    _uiState.update { it.copy(showMessage = true, errorMessage = e.message ?: "Error registrando usuario") }
                }
            )
        }
    }
}
