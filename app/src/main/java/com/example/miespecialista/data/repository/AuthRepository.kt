package com.example.miespecialista.data.repository

import com.example.miespecialista.data.model.UserAccount

// Repositorio encargados del acceso y persistencia simulada de usuarios
class AuthRepository {

    private val usersList = mutableListOf(
        UserAccount(
            fullName = "Carlos Rodríguez",
            email = "carlos@cliente.com",
            password = "Password123!",
            phone = "+52 55 9876 5432",
            address = "Av. Insurgentes Sur 120, Coyoacán"
        )
    )

    fun registerUser(newUser: UserAccount): Result<UserAccount> {
        val existing = usersList.find { it.email.equals(newUser.email.trim(), ignoreCase = true) }
        if (existing != null) {
            return Result.failure(Exception("El correo electrónico ${newUser.email} ya está registrado."))
        }
        usersList.add(newUser)
        return Result.success(newUser)
    }

    fun loginUser(email: String, password: String): Result<UserAccount> {
        val found = usersList.find { it.email.equals(email.trim(), ignoreCase = true) }
            ?: return Result.failure(Exception("El correo electrónico no está registrado. Regístrate primero."))

        if (found.password != password) {
            return Result.failure(Exception("Contraseña incorrecta. Inténtalo de nuevo."))
        }

        return Result.success(found)
    }

    fun updateUser(updatedUser: UserAccount) {
        val index = usersList.indexOfFirst { it.email.equals(updatedUser.email, ignoreCase = true) }
        if (index != -1) {
            usersList[index] = updatedUser
        }
    }
}
