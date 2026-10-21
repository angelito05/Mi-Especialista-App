package com.example.miespecialista.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.miespecialista.data.model.PasswordValidationState
import com.example.miespecialista.data.model.UserAccount
import com.example.miespecialista.data.model.validatePassword
import com.example.miespecialista.data.repository.AuthRepository

// ViewModel para gestionar el estado de Autenticación (MVVM)
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

    // Campos de Login
    var loginEmail by mutableStateOf("")
        private set
    var loginPassword by mutableStateOf("")
        private set
    var loginPasswordVisible by mutableStateOf(false)
        private set

    // Campos de Registro
    var registerFullName by mutableStateOf("")
        private set
    var registerEmail by mutableStateOf("")
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

    fun updateLoginEmail(email: String) {
        loginEmail = email
        errorMessage = null
    }

    fun updateLoginPassword(password: String) {
        loginPassword = password
        errorMessage = null
    }

    fun toggleLoginPasswordVisibility() {
        loginPasswordVisible = !loginPasswordVisible
    }

    fun updateRegisterFullName(fullName: String) {
        registerFullName = fullName
        errorMessage = null
    }

    fun updateRegisterEmail(email: String) {
        registerEmail = email
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

    fun submitLogin() {
        when {
            loginEmail.isBlank() -> {
                errorMessage = "Por favor ingresa tu correo electrónico."
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(loginEmail.trim()).matches() -> {
                errorMessage = "Ingresa un correo electrónico válido."
            }
            loginPassword.isBlank() -> {
                errorMessage = "Por favor ingresa tu contraseña."
            }
            else -> {
                val result = authRepository.loginUser(loginEmail, loginPassword)
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

    fun submitRegister() {
        val cleanName = registerFullName.trim()
        val cleanEmail = registerEmail.trim()

        when {
            cleanName.isBlank() -> {
                errorMessage = "Por favor ingresa tu nombre completo."
            }
            cleanEmail.isBlank() -> {
                errorMessage = "Por favor ingresa tu correo electrónico."
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() -> {
                errorMessage = "Ingresa un correo electrónico válido."
            }
            !registerPasswordState.isValid -> {
                errorMessage = "La contraseña no cumple con todos los requisitos de seguridad."
            }
            registerPassword != registerConfirmPassword -> {
                errorMessage = "Las contraseñas no coinciden."
            }
            else -> {
                val newUser = UserAccount(
                    fullName = cleanName,
                    email = cleanEmail,
                    password = registerPassword
                )
                val result = authRepository.registerUser(newUser)
                result.fold(
                    onSuccess = { user ->
                        errorMessage = null
                        loginEmail = user.email
                        successNotification = "¡Registro exitoso para ${user.fullName}! Ahora puedes iniciar sesión."
                        authMode = AuthMode.LOGIN
                        registerFullName = ""
                        registerEmail = ""
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

    fun logout() {
        loggedInUser = null
        loginPassword = ""
        errorMessage = null
        successNotification = null
    }

    fun updateUserProfile(updatedUser: UserAccount) {
        authRepository.updateUser(updatedUser)
        loggedInUser = updatedUser
    }
}
