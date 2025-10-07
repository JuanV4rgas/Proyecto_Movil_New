package com.example.proyecto_movil.ui.Screens.Register

data class RegisterState(
    val nombrePersona: String = "",
    val nombreUsuario: String = "",
    val email: String = "",
    val password: String = "",
    val bio: String = "",
    val mostrarPassword: Boolean = false,
    val acceptedTerms: Boolean = false,
    val showMessage: Boolean = false,
    val errorMessage: String = "",
    val navigateBack: Boolean = false,
    val navigateToLogin: Boolean = false,
    val navigateAfterRegister: Boolean = false
)
