package com.example.miespecialista.data.repository

import com.example.miespecialista.data.model.ServiceJob
import com.example.miespecialista.data.model.SpecialistData
import com.example.miespecialista.data.model.WalletTransaction

// Repositorio encargado de especialistas, servicios y billetera
class SpecialistRepository {

    private val specialists = listOf(
        SpecialistData(
            id = "1",
            name = "Roberto Gómez",
            specialty = "Electricista Certificado",
            rating = "4.9 ⭐ (120 reseñas)",
            distance = "a 1.2 km",
            hourlyPrice = "$25 / hr",
            avatarSymbol = "👷‍♂️",
            bio = "Especialista en instalaciones eléctricas residenciales e industriales, detección de cortocircuitos y armado de tableros con 8 años de experiencia.",
            jobsCompleted = 142
        ),
        SpecialistData(
            id = "2",
            name = "Marcos Silva",
            specialty = "Carpintero & Mueblista",
            rating = "4.8 ⭐ (98 reseñas)",
            distance = "a 2.5 km",
            hourlyPrice = "$30 / hr",
            avatarSymbol = "🪚",
            bio = "Fabricación y reparación de muebles a medida, barnizado, instalación de puertas, repisas y remodelación en madera.",
            jobsCompleted = 105
        ),
        SpecialistData(
            id = "3",
            name = "Dora Martínez",
            specialty = "Pintura e Impermeabilización",
            rating = "5.0 ⭐ (150 reseñas)",
            distance = "a 0.8 km",
            hourlyPrice = "$20 / hr",
            avatarSymbol = "🎨",
            bio = "Pintura técnica de interiores, fachadas exterior, impermeabilización de azoteas y acabado fino sin manchas.",
            jobsCompleted = 168
        ),
        SpecialistData(
            id = "4",
            name = "Luis Hernández",
            specialty = "Plomería & Fontanería",
            rating = "4.9 ⭐ (85 reseñas)",
            distance = "a 3.1 km",
            hourlyPrice = "$28 / hr",
            avatarSymbol = "🚰",
            bio = "Reparación de fugas de agua, desazolve de tuberías, instalación de sanitarios, grifería y calentadores de agua.",
            jobsCompleted = 92
        )
    )

    private val services = mutableListOf(
        ServiceJob("1", "Roberto Gómez", "Instalación de Cableado y Tablero Eléctrico", "Electricidad", "Mañana, 10:30 AM", "Confirmado", "$25.00"),
        ServiceJob("2", "Luis Hernández", "Reparación de Fuga de Agua e Inodoro", "Plomería", "Ayer, 3:00 PM", "Completado", "$28.00")
    )

    private val transactions = mutableListOf(
        WalletTransaction("1", "Pago de servicio - Roberto Gómez", "Ayer", "- $25.00", true),
        WalletTransaction("2", "Recarga de saldo", "10 de Octubre", "+ $50.00", false)
    )

    fun getSpecialists(): List<SpecialistData> = specialists

    fun getServices(): List<ServiceJob> = services

    fun getTransactions(): List<WalletTransaction> = transactions
}
