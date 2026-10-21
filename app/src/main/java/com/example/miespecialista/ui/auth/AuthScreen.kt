package com.example.miespecialista.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miespecialista.R
import com.example.miespecialista.data.model.UserAccount
import com.example.miespecialista.ui.main.MainAppScreen
import com.example.miespecialista.ui.theme.Ambar
import com.example.miespecialista.ui.theme.AzulProfundo
import com.example.miespecialista.ui.theme.Blanco
import com.example.miespecialista.ui.theme.GrisSuave
import com.example.miespecialista.ui.theme.GrisTexto
import com.example.miespecialista.ui.theme.MiEspecialistaTheme
import com.example.miespecialista.ui.theme.Turquesa

enum class AuthMode {
    LOGIN,
    REGISTER
}

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    val loggedInUser = authViewModel.loggedInUser

    if (loggedInUser != null) {
        MainAppScreen(
            user = loggedInUser,
            onLogout = { authViewModel.logout() },
            onUpdateUser = { updated -> authViewModel.updateUserProfile(updated) },
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
                // Logo Oficial de la aplicación (code_generated_image__1_)
                Image(
                    painter = painterResource(id = R.drawable.code_generated_image__1_),
                    contentDescription = "Logo Mi Especialista",
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(95.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (authViewModel.authMode == AuthMode.LOGIN) "Encuentra electricistas, carpinteros y más" else "Crea una cuenta para contratar especialistas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Pantalla condicional (Login o Registro) gestionada por MVVM ViewModel
                if (authViewModel.authMode == AuthMode.LOGIN) {
                    LoginFormView(viewModel = authViewModel)
                } else {
                    RegisterFormView(viewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun LoginFormView(
    viewModel: AuthViewModel
) {
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
            AnimatedVisibility(visible = viewModel.successNotification != null) {
                viewModel.successNotification?.let {
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
                value = viewModel.loginEmail,
                onValueChange = { viewModel.updateLoginEmail(it) },
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
                value = viewModel.loginPassword,
                onValueChange = { viewModel.updateLoginPassword(it) },
                label = { Text("Contraseña") },
                leadingIcon = { Text("🔒", fontSize = 18.sp) },
                trailingIcon = {
                    TextButton(onClick = { viewModel.toggleLoginPasswordVisibility() }) {
                        Text(
                            text = if (viewModel.loginPasswordVisible) "Ocultar" else "Mostrar",
                            style = MaterialTheme.typography.bodySmall,
                            color = Turquesa,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                visualTransformation = if (viewModel.loginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

            AnimatedVisibility(visible = viewModel.errorMessage != null) {
                viewModel.errorMessage?.let {
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
                onClick = { viewModel.submitLogin() },
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
            TextButton(onClick = { viewModel.switchToRegister() }) {
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
fun RegisterFormView(
    viewModel: AuthViewModel
) {
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
                value = viewModel.registerFullName,
                onValueChange = { viewModel.updateRegisterFullName(it) },
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
                value = viewModel.registerEmail,
                onValueChange = { viewModel.updateRegisterEmail(it) },
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
                value = viewModel.registerPassword,
                onValueChange = { viewModel.updateRegisterPassword(it) },
                label = { Text("Contraseña") },
                leadingIcon = { Text("🔒", fontSize = 18.sp) },
                trailingIcon = {
                    TextButton(onClick = { viewModel.toggleRegisterPasswordVisibility() }) {
                        Text(
                            text = if (viewModel.registerPasswordVisible) "Ocultar" else "Mostrar",
                            style = MaterialTheme.typography.bodySmall,
                            color = Turquesa,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                visualTransformation = if (viewModel.registerPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

                    val pState = viewModel.registerPasswordState
                    PasswordRequirementItem(
                        label = "Mínimo 8 caracteres",
                        isMet = pState.hasMinLength
                    )
                    PasswordRequirementItem(
                        label = "Al menos una letra mayúscula (A-Z)",
                        isMet = pState.hasUppercase
                    )
                    PasswordRequirementItem(
                        label = "Al menos una letra minúscula (a-z)",
                        isMet = pState.hasLowercase
                    )
                    PasswordRequirementItem(
                        label = "Al menos un número (0-9)",
                        isMet = pState.hasDigit
                    )
                    PasswordRequirementItem(
                        label = "Al menos un símbolo especial (!@#$%^&*...)",
                        isMet = pState.hasSpecialChar
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Confirmar Contraseña
            OutlinedTextField(
                value = viewModel.registerConfirmPassword,
                onValueChange = { viewModel.updateRegisterConfirmPassword(it) },
                label = { Text("Confirmar Contraseña") },
                leadingIcon = { Text("🔐", fontSize = 18.sp) },
                trailingIcon = {
                    TextButton(onClick = { viewModel.toggleRegisterConfirmPasswordVisibility() }) {
                        Text(
                            text = if (viewModel.registerConfirmPasswordVisible) "Ocultar" else "Mostrar",
                            style = MaterialTheme.typography.bodySmall,
                            color = Turquesa,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                visualTransformation = if (viewModel.registerConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

            AnimatedVisibility(visible = viewModel.errorMessage != null) {
                viewModel.errorMessage?.let {
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
                onClick = { viewModel.submitRegister() },
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
            TextButton(onClick = { viewModel.switchToLogin() }) {
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
