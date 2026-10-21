package com.example.miespecialista.data.model

// Modelo de datos para Especialistas de Servicios
data class SpecialistData(
    val id: String,
    val name: String,
    val specialty: String,
    val rating: String,
    val distance: String,
    val hourlyPrice: String,
    val avatarSymbol: String,
    val bio: String,
    val jobsCompleted: Int
)

// Modelo de datos para servicios contratados y transacciones
data class ServiceJob(
    val id: String,
    val specialistName: String,
    val serviceTitle: String,
    val category: String,
    val date: String,
    val status: String,
    val cost: String
)

data class WalletTransaction(
    val id: String,
    val title: String,
    val date: String,
    val amount: String,
    val isNegative: Boolean
)
