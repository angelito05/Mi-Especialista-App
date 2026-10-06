package com.example.miespecialista.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miespecialista.ui.main.MainAppScreen
import com.example.miespecialista.ui.theme.Ambar
import com.example.miespecialista.ui.theme.AzulProfundo
import com.example.miespecialista.ui.theme.Blanco
import com.example.miespecialista.ui.theme.GrisSuave
import com.example.miespecialista.ui.theme.GrisTexto
import com.example.miespecialista.ui.theme.MiEspecialistaTheme
import com.example.miespecialista.ui.theme.Turquesa

// Modelo de datos para la cuenta de usuario registrada
data class UserAccount(
    val fullName: String,
    val email: String,
    val password: String
)

// Requisitos de validación de contraseña
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

enum class AuthMode {
    LOGIN,
    REGISTER
}

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier
) {
    // Lista de usuarios registrados simulados en la aplicación
    var registeredUsers by remember {
        mutableStateOf(
            listOf(
                UserAccount(
                    fullName = "Carlos Rodríguez",
                    email = "carlos@cliente.com",
                    password = "Password123!"
                )
            )
        )
    }

    var authMode by remember { mutableStateOf(AuthMode.LOGIN) }
    var loggedInUser by remember { mutableStateOf<UserAccount?>(null) }
    var prefilledEmail by remember { mutableStateOf("") }
    var successNotification by remember { mutableStateOf<String?>(null) }

    if (loggedInUser != null) {
        MainAppScreen(
            user = loggedInUser!!,
            onLogout = { loggedInUser = null },
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Header Logo
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Turquesa),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🛠️",
                        fontSize = 40.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Mi Especialista",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = AzulProfundo
                )

                Text(
                    text = if (authMode == AuthMode.LOGIN) "Encuentra electricistas, carpinteros y más" else "Crea una cuenta para contratar especialistas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Pantalla condicional (Login o Registro) según la selección del usuario
                if (authMode == AuthMode.LOGIN) {
                    LoginForm(
                        registeredUsers = registeredUsers,
                        initialEmail = prefilledEmail,
                        successMessage = successNotification,
                        onLoginSuccess = { user -> loggedInUser = user },
                        onSwitchToRegister = {
                            authMode = AuthMode.REGISTER
                            successNotification = null
                        }
                    )
                } else {
                    RegisterForm(
                        registeredUsers = registeredUsers,
                        onRegisterSuccess = { newUser ->
                            registeredUsers = registeredUsers + newUser
                            prefilledEmail = newUser.email
                            successNotification = "¡Registro exitoso para ${newUser.fullName}! Ahora puedes iniciar sesión."
                            authMode = AuthMode.LOGIN // Redirigir a inicio de sesión tras registrarse
                        },
                        onSwitchToLogin = {
                            authMode = AuthMode.LOGIN
                            successNotification = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LoginForm(
    registeredUsers: List<UserAccount>,
    initialEmail: String,
    successMessage: String?,
    onLoginSuccess: (UserAccount) -> Unit,
    onSwitchToRegister: () -> Unit
) {
    var email by remember(initialEmail) { mutableStateOf(initialEmail) }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GrisSuave, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AzulProfundo
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje de éxito tras registro
            AnimatedVisibility(visible = successMessage != null) {
                successMessage?.let {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Turquesa.copy(alpha = 0.15f)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = it,
                            color = AzulProfundo,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            // Correo
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Text("✉️", fontSize = 18.sp) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Contraseña") },
                leadingIcon = { Text("🔒", fontSize = 18.sp) },
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "Ocultar" else "Mostrar",
                            style = MaterialTheme.typography.bodySmall,
                            color = Turquesa,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(visible = errorMessage != null) {
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de Iniciar Sesión
            Button(
                onClick = {
                    when {
                        email.isBlank() -> {
                            errorMessage = "Por favor ingresa tu correo electrónico."
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            errorMessage = "Ingresa un correo electrónico válido."
                        }
                        password.isBlank() -> {
                            errorMessage = "Por favor ingresa tu contraseña."
                        }
                        else -> {
                            val foundUser = registeredUsers.find {
                                it.email.equals(email.trim(), ignoreCase = true)
                            }

                            if (foundUser == null) {
                                errorMessage = "El correo electrónico no está registrado. Regístrate primero."
                            } else if (foundUser.password != password) {
                                errorMessage = "Contraseña incorrecta. Inténtalo de nuevo."
                            } else {
                                errorMessage = null
                                onLoginSuccess(foundUser)
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulProfundo,
                    contentColor = Blanco
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Enlace interactivo para ir al Formulario de Registro
            TextButton(onClick = onSwitchToRegister) {
                Text(
                    text = "¿No tienes una cuenta? Regístrate aquí",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Turquesa,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RegisterForm(
    registeredUsers: List<UserAccount>,
    onRegisterSuccess: (UserAccount) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val passwordState = remember(password) { validatePassword(password) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GrisSuave, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AzulProfundo
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre Completo
            OutlinedTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    errorMessage = null
                },
                label = { Text("Nombre Completo") },
                leadingIcon = { Text("👤", fontSize = 18.sp) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Correo
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = { Text("Correo Electrónico") },
                leadingIcon = { Text("✉️", fontSize = 18.sp) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Contraseña
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = { Text("Contraseña") },
                leadingIcon = { Text("🔒", fontSize = 18.sp) },
                trailingIcon = {
                    TextButton(onClick = { passwordVisible = !passwordVisible }) {
                        Text(
                            text = if (passwordVisible) "Ocultar" else "Mostrar",
                            style = MaterialTheme.typography.bodySmall,
                            color = Turquesa,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Cuadro de requisitos de contraseña
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = GrisSuave)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Requisitos de seguridad para la contraseña:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = AzulProfundo
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    PasswordRequirementItem(
                        label = "Mínimo 8 caracteres",
                        isMet = passwordState.hasMinLength
                    )
                    PasswordRequirementItem(
                        label = "Al menos una letra mayúscula (A-Z)",
                        isMet = passwordState.hasUppercase
                    )
                    PasswordRequirementItem(
                        label = "Al menos una letra minúscula (a-z)",
                        isMet = passwordState.hasLowercase
                    )
                    PasswordRequirementItem(
                        label = "Al menos un número (0-9)",
                        isMet = passwordState.hasDigit
                    )
                    PasswordRequirementItem(
                        label = "Al menos un símbolo especial (!@#$%^&*...)",
                        isMet = passwordState.hasSpecialChar
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Confirmar Contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    errorMessage = null
                },
                label = { Text("Confirmar Contraseña") },
                leadingIcon = { Text("🔐", fontSize = 18.sp) },
                trailingIcon = {
                    TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Text(
                            text = if (confirmPasswordVisible) "Ocultar" else "Mostrar",
                            style = MaterialTheme.typography.bodySmall,
                            color = Turquesa,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Turquesa,
                    unfocusedBorderColor = GrisSuave,
                    focusedLabelColor = AzulProfundo
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(visible = errorMessage != null) {
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de Registro
            Button(
                onClick = {
                    val cleanEmail = email.trim()
                    val cleanName = fullName.trim()

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
                        registeredUsers.any { it.email.equals(cleanEmail, ignoreCase = true) } -> {
                            errorMessage = "El correo electrónico $cleanEmail ya está registrado."
                        }
                        !passwordState.isValid -> {
                            errorMessage = "La contraseña no cumple con todos los requisitos de seguridad."
                        }
                        password != confirmPassword -> {
                            errorMessage = "Las contraseñas no coinciden."
                        }
                        else -> {
                            errorMessage = null
                            val newUser = UserAccount(
                                fullName = cleanName,
                                email = cleanEmail,
                                password = password
                            )
                            onRegisterSuccess(newUser)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulProfundo,
                    contentColor = Blanco
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Registrarse",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Enlace interactivo para regresar a Iniciar Sesión
            TextButton(onClick = onSwitchToLogin) {
                Text(
                    text = "¿Ya tienes una cuenta? Inicia sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Turquesa,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PasswordRequirementItem(
    label: String,
    isMet: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = if (isMet) "✓" else "✕",
            color = if (isMet) Turquesa else Ambar,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (isMet) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isMet) AzulProfundo else GrisTexto
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    MiEspecialistaTheme {
        AuthScreen()
    }
}
