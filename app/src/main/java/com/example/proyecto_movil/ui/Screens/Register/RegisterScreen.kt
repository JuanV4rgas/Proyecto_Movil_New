package com.example.proyecto_movil.ui.Screens.Register

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyecto_movil.R
import com.example.proyecto_movil.ui.utils.*
import androidx.compose.foundation.layout.imePadding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RegisterScreen(
    viewModel: Any,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onRegister: (String, String, String) -> Unit = { _, _, _ -> },
    onLogin: () -> Unit = {}
) {
    val actualViewModel = when (viewModel) {
        is RegisterViewModelLike -> viewModel
        is RegisterViewModel -> object : RegisterViewModelLike {
            override val uiState: StateFlow<RegisterState> = viewModel.uiState
            override fun updateNombrePersona(value: String) = viewModel.updateNombrePersona(value)
            override fun updateNombreUsuario(value: String) = viewModel.updateNombreUsuario(value)
            override fun updateEmail(value: String) = viewModel.updateEmail(value)
            override fun updatePassword(value: String) = viewModel.updatePassword(value)
            override fun toggleMostrarPassword() = viewModel.toggleMostrarPassword()
            override fun toggleAcceptedTerms() = viewModel.toggleAcceptedTerms()
            override fun onRegisterClicked() = viewModel.onRegisterClicked()
            override fun onLoginClicked() = viewModel.onLoginClicked()
            override fun onBackClicked() = viewModel.onBackClicked()
            override fun consumeNavigation() = viewModel.consumeNavigation()
            override fun consumeMessage() = viewModel.consumeMessage()
        }
        else -> throw IllegalArgumentException("RegisterScreen necesita un RegisterViewModel o RegisterViewModelLike")
    }

    val state by actualViewModel.uiState.collectAsState()
    val isDark = isSystemInDarkTheme()
    val backgroundRes = if (isDark) R.drawable.fondocriti else R.drawable.fondocriti_light

    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar snackbar de manera segura
    LaunchedEffect(state.showMessage, state.errorMessage) {
        if (state.showMessage) {
            state.errorMessage?.let { msg ->
                if (msg.isNotBlank()) snackbarHostState.showSnackbar(message = msg)
            }
            actualViewModel.consumeMessage()
        }
    }

    // Dejo la navegación derivada por compatibilidad
    LaunchedEffect(state.navigateBack, state.navigateToLogin, state.navigateAfterRegister) {
        when {
            state.navigateBack -> {
                onBack()
                actualViewModel.consumeNavigation()
            }
            state.navigateToLogin -> {
                onLogin()
                actualViewModel.consumeNavigation()
            }
            state.navigateAfterRegister -> {
                onRegister(state.nombreUsuario, state.email, state.password)
                actualViewModel.consumeNavigation()
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = backgroundRes),
                contentDescription = stringResource(id = R.string.fondo_degradado),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = {
                    onBack()                      // navegar YA
                    actualViewModel.onBackClicked() // opcional: notificar al VM
                },
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
            ) {
                Spacer(Modifier.height(80.dp))
                LogoApp()
                Spacer(Modifier.height(60.dp))
                Registrate(texto = "Regístrate")
                Spacer(Modifier.height(10.dp))

                FormularioRegistro(
                    nombrePersona = state.nombrePersona,
                    nombreUsuario = state.nombreUsuario,
                    email = state.email,
                    password = state.password,
                    mostrarPassword = state.mostrarPassword,
                    onNombrePersonaChange = actualViewModel::updateNombrePersona,
                    onNombreUsuarioChange = actualViewModel::updateNombreUsuario,
                    onEmailChange = actualViewModel::updateEmail,
                    onPasswordChange = actualViewModel::updatePassword,
                    onTogglePassword = actualViewModel::toggleMostrarPassword
                )

                Spacer(Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.acceptedTerms,
                        onCheckedChange = { actualViewModel.toggleAcceptedTerms() }
                    )
                    Spacer(Modifier.width(8.dp))
                    Terminos(
                        texto = "He leído y acepto los términos y condiciones",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                AppButton(
                    text = "Registrarse",
                    onClick = { actualViewModel.onRegisterClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(30.dp))

                // Navega directo al Login
                YatienesCuenta(
                    texto = "¿Ya tienes una cuenta? Inicia sesión",
                    onClick = {
                        onLogin()                        // navegar YA
                        actualViewModel.onLoginClicked() // opcional
                    }
                )

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun FormularioRegistro(
    nombrePersona: String,
    nombreUsuario: String,
    email: String,
    password: String,
    mostrarPassword: Boolean,
    onNombrePersonaChange: (String) -> Unit,
    onNombreUsuarioChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        OutlinedTextField(
            value = nombrePersona,
            onValueChange = onNombrePersonaChange,
            label = { Text(stringResource(R.string.nombre)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.usuario),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Icono de usuario",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nombreUsuario,
            onValueChange = onNombreUsuarioChange,
            label = { Text(stringResource(R.string.nombre_usuario)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.usuario),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Icono de usuario",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.correo),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Icono de correo",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(stringResource(R.string.contra)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.candado),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Icono de candado",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            visualTransformation = if (mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        painter = painterResource(if (mostrarPassword) R.drawable.view else R.drawable.hide),
                        contentDescription = stringResource(R.string.mostrar_password),
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

/** Contrato mínimo que el RegisterScreen necesita. */
interface RegisterViewModelLike {
    val uiState: StateFlow<RegisterState>
    fun updateNombrePersona(value: String)
    fun updateNombreUsuario(value: String)
    fun updateEmail(value: String)
    fun updatePassword(value: String)
    fun toggleMostrarPassword()
    fun toggleAcceptedTerms()
    fun onRegisterClicked()
    fun onLoginClicked()
    fun onBackClicked()
    fun consumeNavigation()
    fun consumeMessage()
}

/** Solo para preview local. */
class FakeRegisterViewModel : RegisterViewModelLike {
    private val _uiState = MutableStateFlow(RegisterState())
    override val uiState: StateFlow<RegisterState> = _uiState

    override fun updateNombrePersona(value: String) {}
    override fun updateNombreUsuario(value: String) {}
    override fun updateEmail(value: String) {}
    override fun updatePassword(value: String) {}
    override fun toggleMostrarPassword() {}
    override fun toggleAcceptedTerms() {}
    override fun onRegisterClicked() {}
    override fun onLoginClicked() {}
    override fun onBackClicked() {}
    override fun consumeNavigation() {}
    override fun consumeMessage() {}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    MaterialTheme {
        RegisterScreen(
            viewModel = FakeRegisterViewModel(),
            onBack = {},
            onRegister = { _, _, _ -> },
            onLogin = {}
        )
    }
}
