package com.example.miespecialista.data.repository

import com.example.miespecialista.data.model.UserAccount
import com.example.miespecialista.data.remote.ApiService
import com.example.miespecialista.data.remote.LoginRequest
import com.example.miespecialista.data.remote.RegisterRequest
import com.example.miespecialista.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repositorio de Autenticación conectado a la API de la BD con soporte Offline/Fallback
class AuthRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    private val localUsers = mutableListOf(
        UserAccount(
            fullName = "Carlos Rodríguez",
            email = "carlos@cliente.com",
            password = "Password123!",
            phone = "+52 55 9876 5432",
            address = "Av. Insurgentes Sur 120, Coyoacán"
        )
    )

    suspend fun registerUser(newUser: UserAccount): Result<UserAccount> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(
                RegisterRequest(
                    fullName = newUser.fullName,
                    email = newUser.email,
                    password = newUser.password,
                    phone = newUser.phone,
                    address = newUser.address
                )
            )

            if (response.isSuccessful && response.body()?.success == true) {
                val user = response.body()?.data ?: newUser
                localUsers.add(user)
                Result.success(user)
            } else {
                // Fallback local en desarrollo/offline
                registerLocal(newUser)
            }
        } catch (e: Exception) {
            // Manejo de ausencia de conexión API -> Fallback a memoria local
            registerLocal(newUser)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<UserAccount> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(LoginRequest(email.trim(), password))

            if (response.isSuccessful && response.body()?.success == true) {
                val user = response.body()?.data ?: loginLocal(email, password).getOrThrow()
                Result.success(user)
            } else {
                loginLocal(email, password)
            }
        } catch (e: Exception) {
            // Manejo local offline cuando no hay API activa
            loginLocal(email, password)
        }
    }

    suspend fun updateUser(updatedUser: UserAccount) = withContext(Dispatchers.IO) {
        try {
            apiService.updateProfile(updatedUser)
        } catch (_: Exception) {
            // Manejo offline silencioso
        }
        val index = localUsers.indexOfFirst { it.email.equals(updatedUser.email, ignoreCase = true) }
        if (index != -1) {
            localUsers[index] = updatedUser
        }
    }

    private fun registerLocal(newUser: UserAccount): Result<UserAccount> {
        val existing = localUsers.find { it.email.equals(newUser.email.trim(), ignoreCase = true) }
        if (existing != null) {
            return Result.failure(Exception("El correo electrónico ${newUser.email} ya está registrado."))
        }
        localUsers.add(newUser)
        return Result.success(newUser)
    }

    private fun loginLocal(email: String, password: String): Result<UserAccount> {
        val found = localUsers.find { it.email.equals(email.trim(), ignoreCase = true) }
            ?: return Result.failure(Exception("El correo electrónico no está registrado. Regístrate primero."))

        if (found.password != password) {
            return Result.failure(Exception("Contraseña incorrecta. Inténtalo de nuevo."))
        }

        return Result.success(found)
    }
}
