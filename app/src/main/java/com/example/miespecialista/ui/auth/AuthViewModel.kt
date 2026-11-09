package com.example.miespecialista.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miespecialista.data.model.PasswordValidationState
import com.example.miespecialista.data.model.UserAccount
import com.example.miespecialista.data.model.validatePassword
import com.example.miespecialista.data.repository.AuthRepository
import kotlinx.coroutines.launch

// ViewModel para gestionar el estado de Autenticación con llamadas asíncronas a la API (MVVM)
class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var authMode by mutableStateOf(AuthMode.LOGIN)
        private set

    var loggedInUser by mutableStateOf<UserAccount?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successNotification by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    // ─── Campos de Login ──────────────────────────────────────────────────────

    var loginTelefono by mutableStateOf("")
        private set
    var loginPassword by mutableStateOf("")
        private set
    var loginPasswordVisible by mutableStateOf(false)
        private set

    // ─── Campos de Registro ───────────────────────────────────────────────────

    var registerTelefono by mutableStateOf("")
        private set
    var registerPassword by mutableStateOf("")
        private set
    var registerConfirmPassword by mutableStateOf("")
        private set
    var registerPasswordVisible by mutableStateOf(false)
        private set
    var registerConfirmPasswordVisible by mutableStateOf(false)
        private set

    val registerPasswordState: PasswordValidationState
        get() = validatePassword(registerPassword)

    // ─── Actualizadores de Login ──────────────────────────────────────────────

    fun updateLoginTelefono(telefono: String) {
        loginTelefono = telefono
        errorMessage = null
    }

    fun updateLoginPassword(password: String) {
        loginPassword = password
        errorMessage = null
    }

    fun toggleLoginPasswordVisibility() {
        loginPasswordVisible = !loginPasswordVisible
    }

    // ─── Actualizadores de Registro ───────────────────────────────────────────

    fun updateRegisterTelefono(telefono: String) {
        registerTelefono = telefono
        errorMessage = null
    }

    fun updateRegisterPassword(password: String) {
        registerPassword = password
        errorMessage = null
    }

    fun updateRegisterConfirmPassword(confirmPassword: String) {
        registerConfirmPassword = confirmPassword
        errorMessage = null
    }

    fun toggleRegisterPasswordVisibility() {
        registerPasswordVisible = !registerPasswordVisible
    }

    fun toggleRegisterConfirmPasswordVisibility() {
        registerConfirmPasswordVisible = !registerConfirmPasswordVisible
    }

    // ─── Navegación entre formularios ─────────────────────────────────────────

    fun switchToRegister() {
        authMode = AuthMode.REGISTER
        errorMessage = null
        successNotification = null
    }

    fun switchToLogin() {
        authMode = AuthMode.LOGIN
        errorMessage = null
        successNotification = null
    }

    // ─── Acciones de sesión ───────────────────────────────────────────────────

    fun submitLogin() {
        val cleanTelefono = loginTelefono.trim()
        when {
            cleanTelefono.isBlank() -> {
                errorMessage = "Por favor ingresa tu número de teléfono."
            }
            cleanTelefono.length < 7 -> {
                errorMessage = "Ingresa un número de teléfono válido (mínimo 7 dígitos)."
            }
            loginPassword.isBlank() -> {
                errorMessage = "Por favor ingresa tu contraseña."
            }
            else -> {
                viewModelScope.launch {
                    isLoading = true
                    val result = authRepository.loginUser(cleanTelefono, loginPassword)
                    isLoading = false
                    result.fold(
                        onSuccess = { user ->
                            errorMessage = null
                            loggedInUser = user
                        },
                        onFailure = { exception ->
                            errorMessage = exception.message
                        }
                    )
                }
            }
        }
    }

    fun submitRegister() {
        val cleanTelefono = registerTelefono.trim()

        when {
            cleanTelefono.isBlank() -> {
                errorMessage = "Por favor ingresa tu número de teléfono."
            }
            cleanTelefono.length < 7 -> {
                errorMessage = "Ingresa un número de teléfono válido (mínimo 7 dígitos)."
            }
            !registerPasswordState.isValid -> {
                errorMessage = "La contraseña no cumple con todos los requisitos de seguridad."
            }
            registerPassword != registerConfirmPassword -> {
                errorMessage = "Las contraseñas no coinciden."
            }
            else -> {
                viewModelScope.launch {
                    isLoading = true
                    val result = authRepository.registerUser(
                        telefono = cleanTelefono,
                        password = registerPassword,
                        rol = "cliente"
                    )
                    isLoading = false
                    result.fold(
                        onSuccess = { user ->
                            errorMessage = null
                            loginTelefono = user.telefono
                            successNotification = "¡Registro exitoso! Ahora puedes iniciar sesión."
                            authMode = AuthMode.LOGIN
                            registerTelefono = ""
                            registerPassword = ""
                            registerConfirmPassword = ""
                        },
                        onFailure = { exception ->
                            errorMessage = exception.message
                        }
                    )
                }
            }
        }
    }

    fun logout() {
        authRepository.logout()
        loggedInUser = null
        loginPassword = ""
        errorMessage = null
        successNotification = null
    }

    fun updateUserProfile(updatedUser: UserAccount) {
        viewModelScope.launch {
            authRepository.updateUser(updatedUser)
            loggedInUser = updatedUser
        }
    }
}
