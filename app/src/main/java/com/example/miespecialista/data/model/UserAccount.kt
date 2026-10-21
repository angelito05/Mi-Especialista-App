package com.example.miespecialista.data.model

// Modelo de datos para la cuenta de usuario registrada
data class UserAccount(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String = "+52 55 1234 5678",
    val address: String = "Av. Principal #45, Depto 201"
)

// Estado de validación de los requisitos de contraseña
data class PasswordValidationState(
    val hasMinLength: Boolean = false,
    val hasUppercase: Boolean = false,
    val hasLowercase: Boolean = false,
    val hasDigit: Boolean = false,
    val hasSpecialChar: Boolean = false
) {
    val isValid: Boolean
        get() = hasMinLength && hasUppercase && hasLowercase && hasDigit && hasSpecialChar
}

fun validatePassword(password: String): PasswordValidationState {
    return PasswordValidationState(
        hasMinLength = password.length >= 8,
        hasUppercase = password.any { it.isUpperCase() },
        hasLowercase = password.any { it.isLowerCase() },
        hasDigit = password.any { it.isDigit() },
        hasSpecialChar = password.any { !it.isLetterOrDigit() }
    )
}
