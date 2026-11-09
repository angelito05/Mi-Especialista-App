package com.example.miespecialista.data.repository

import com.example.miespecialista.data.model.ServiceJob
import com.example.miespecialista.data.model.SpecialistData
import com.example.miespecialista.data.model.WalletTransaction
import com.example.miespecialista.data.remote.ApiService
import com.example.miespecialista.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Alias de compatibilidad hacia MainRepository. Mantiene los métodos originales
// para que el código de MainViewModel compilado antes de la migración siga funcionando.
class SpecialistRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {
    // Delegamos al nuevo repositorio unificado
    private val mainRepository = MainRepository(apiService)

    fun getLocalSpecialists(): List<SpecialistData> = mainRepository.getLocalSpecialists()

    suspend fun getSpecialists(query: String? = null): List<SpecialistData> = withContext(Dispatchers.IO) {
        // No hay endpoint real de búsqueda de especialistas → usar fallback local
        val list = mainRepository.getLocalSpecialists()
        if (query.isNullOrBlank()) list
        else list.filter {
            it.name.lowercase().contains(query.lowercase()) ||
            it.specialty.lowercase().contains(query.lowercase())
        }
    }

    fun getServices(): List<ServiceJob> = mainRepository.getServices()

    fun getTransactions(): List<WalletTransaction> = mainRepository.getTransactions()
}
