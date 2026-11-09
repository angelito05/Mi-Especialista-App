package com.example.miespecialista.data.repository

import com.example.miespecialista.data.model.UserAccount
import com.example.miespecialista.data.remote.ApiService
import com.example.miespecialista.data.remote.LoginRequest
import com.example.miespecialista.data.remote.RegisterRequest
import com.example.miespecialista.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repositorio de Autenticación conectado a la API real con soporte Offline/Fallback
class AuthRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    // Usuario demo local para modo offline (teléfono y contraseña del backend de prueba)
    private val localUsers = mutableListOf(
        UserAccount(
            usuarioId = 1L,
            telefono = "7441234567",
            rol = "cliente",
            estadoCuenta = "activo",
            password = "Password123!"
        )
    )

    suspend fun registerUser(
        telefono: String,
        password: String,
        rol: String? = null,
        fotoPerfil: String? = null
    ): Result<UserAccount> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.registro(
                RegisterRequest(
                    telefono = telefono.trim(),
                    password = password,
                    rol = rol,
                    fotoPerfil = fotoPerfil
                )
            )

            if (response.isSuccessful) {
                val body = response.body()
                // Guardar JWT token recibido
                body?.token?.let { RetrofitClient.authToken = it }
                val user = body?.usuario ?: UserAccount(telefono = telefono.trim(), rol = rol ?: "cliente")
                localUsers.add(user.copy(password = password))
                Result.success(user)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error en el registro"
                registerLocal(telefono, password, rol)
                    .recoverCatching { throw Exception(errorMsg) }
            }
        } catch (e: Exception) {
            // Fallback a memoria local en caso de red no disponible
            registerLocal(telefono, password, rol)
        }
    }

    suspend fun loginUser(telefono: String, password: String): Result<UserAccount> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(LoginRequest(telefono.trim(), password))

            if (response.isSuccessful) {
                val body = response.body()
                // Guardar JWT token recibido
                body?.token?.let { RetrofitClient.authToken = it }
                val user = body?.usuario ?: loginLocal(telefono, password).getOrThrow()
                Result.success(user)
            } else {
                loginLocal(telefono, password)
            }
        } catch (e: Exception) {
            // Fallback offline cuando no hay conexión a la API
            loginLocal(telefono, password)
        }
    }

    suspend fun updateUser(updatedUser: UserAccount) = withContext(Dispatchers.IO) {
        // Actualizar en lista local
        val index = localUsers.indexOfFirst { it.telefono == updatedUser.telefono }
        if (index != -1) {
            localUsers[index] = updatedUser
        }
    }

    fun logout() {
        RetrofitClient.authToken = null
    }

    // ─── Funciones de fallback local ──────────────────────────────────────────

    private fun registerLocal(telefono: String, password: String, rol: String?): Result<UserAccount> {
        val existing = localUsers.find { it.telefono == telefono.trim() }
        if (existing != null) {
            return Result.failure(Exception("El número de teléfono ${telefono} ya está registrado."))
        }
        val newUser = UserAccount(
            telefono = telefono.trim(),
            rol = rol ?: "cliente",
            estadoCuenta = "activo",
            password = password
        )
        localUsers.add(newUser)
        return Result.success(newUser)
    }

    private fun loginLocal(telefono: String, password: String): Result<UserAccount> {
        val found = localUsers.find { it.telefono == telefono.trim() }
            ?: return Result.failure(Exception("El número de teléfono no está registrado. Regístrate primero."))

        if (found.password != password) {
            return Result.failure(Exception("Contraseña incorrecta. Inténtalo de nuevo."))
        }

        return Result.success(found)
    }
}
