package com.example.miespecialista.data.repository

import com.example.miespecialista.data.model.Categoria
import com.example.miespecialista.data.model.Incidencia
import com.example.miespecialista.data.model.Mensaje
import com.example.miespecialista.data.model.Movimiento
import com.example.miespecialista.data.model.Resena
import com.example.miespecialista.data.model.ServiceJob
import com.example.miespecialista.data.model.SpecialistData
import com.example.miespecialista.data.model.Solicitud
import com.example.miespecialista.data.model.WalletTransaction
import com.example.miespecialista.data.remote.ActualizarSolicitudRequest
import com.example.miespecialista.data.remote.ApiService
import com.example.miespecialista.data.remote.CrearIncidenciaRequest
import com.example.miespecialista.data.remote.CrearMovimientoRequest
import com.example.miespecialista.data.remote.CrearResenaRequest
import com.example.miespecialista.data.remote.CrearSolicitudRequest
import com.example.miespecialista.data.remote.EnviarMensajeRequest
import com.example.miespecialista.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Repositorio principal: combina datos remotos de la API con fallback local
class MainRepository(
    private val apiService: ApiService = RetrofitClient.apiService
) {

    // ─── Datos locales de Especialistas (fallback, no hay endpoint real) ──────

    private val localSpecialists = listOf(
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

    // ─── Datos locales de Servicios y Transacciones (UI legacy) ───────────────

    private val localServices = listOf(
        ServiceJob("1", "Roberto Gómez", "Instalación de Cableado y Tablero Eléctrico", "Electricidad", "Mañana, 10:30 AM", "Confirmado", "$25.00"),
        ServiceJob("2", "Luis Hernández", "Reparación de Fuga de Agua e Inodoro", "Plomería", "Ayer, 3:00 PM", "Completado", "$28.00")
    )

    private val localTransactions = listOf(
        WalletTransaction("1", "Pago de servicio - Roberto Gómez", "Ayer", "- $25.00", true),
        WalletTransaction("2", "Recarga de saldo", "10 de Octubre", "+ $50.00", false)
    )

    // ─── Especialistas (fallback local, sin endpoint real) ────────────────────

    fun getLocalSpecialists(): List<SpecialistData> = localSpecialists

    fun getServices(): List<ServiceJob> = localServices

    fun getTransactions(): List<WalletTransaction> = localTransactions

    // ─── Solicitudes ──────────────────────────────────────────────────────────

    suspend fun getSolicitudes(): List<Solicitud> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getSolicitudes()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun crearSolicitud(
        titulo: String,
        descripcion: String? = null,
        direccion: String? = null,
        fechaServicio: String? = null,
        horaServicio: String? = null
    ): Result<Solicitud> = withContext(Dispatchers.IO) {
        val token = RetrofitClient.authToken?.let { "Bearer $it" } ?: ""
        try {
            val response = apiService.crearSolicitud(
                token = token,
                request = CrearSolicitudRequest(
                    titulo = titulo,
                    descripcion = descripcion,
                    direccion = direccion,
                    fechaServicio = fechaServicio,
                    horaServicio = horaServicio
                )
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al crear la solicitud"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarSolicitud(id: Long, estado: String): Result<Solicitud> = withContext(Dispatchers.IO) {
        val token = RetrofitClient.authToken?.let { "Bearer $it" } ?: ""
        try {
            val response = apiService.actualizarSolicitud(
                token = token,
                id = id,
                request = ActualizarSolicitudRequest(estado = estado)
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al actualizar la solicitud"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Categorías ───────────────────────────────────────────────────────────

    suspend fun getCategorias(): List<Categoria> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getCategorias()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // ─── Movimientos ──────────────────────────────────────────────────────────

    suspend fun crearMovimiento(
        tipo: String,
        monto: Double,
        comision: Double? = null,
        solicitudId: Long? = null
    ): Result<Movimiento> = withContext(Dispatchers.IO) {
        val token = RetrofitClient.authToken?.let { "Bearer $it" } ?: ""
        try {
            val response = apiService.crearMovimiento(
                token = token,
                request = CrearMovimientoRequest(
                    tipo = tipo,
                    monto = monto,
                    comision = comision,
                    solicitudId = solicitudId
                )
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al crear movimiento"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Mensajes ─────────────────────────────────────────────────────────────

    suspend fun enviarMensaje(
        receptorId: Long,
        contenido: String,
        solicitudId: Long? = null,
        tipo: String? = null
    ): Result<Mensaje> = withContext(Dispatchers.IO) {
        val token = RetrofitClient.authToken?.let { "Bearer $it" } ?: ""
        try {
            val response = apiService.enviarMensaje(
                token = token,
                request = EnviarMensajeRequest(
                    receptorId = receptorId,
                    contenido = contenido,
                    solicitudId = solicitudId,
                    tipo = tipo
                )
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al enviar mensaje"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMisMensajes(): List<Mensaje> = withContext(Dispatchers.IO) {
        val token = RetrofitClient.authToken?.let { "Bearer $it" } ?: ""
        try {
            val response = apiService.getMisMensajes(token)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    // ─── Reseñas ──────────────────────────────────────────────────────────────

    suspend fun getResenas(): List<Resena> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getResenas()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun crearResena(
        solicitudId: Long? = null,
        colaboradorId: Long,
        calificacion: Int,
        comentario: String? = null
    ): Result<Resena> = withContext(Dispatchers.IO) {
        val token = RetrofitClient.authToken?.let { "Bearer $it" } ?: ""
        try {
            val response = apiService.crearResena(
                token = token,
                request = CrearResenaRequest(
                    solicitudId = solicitudId,
                    colaboradorId = colaboradorId,
                    calificacion = calificacion,
                    comentario = comentario
                )
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al crear reseña"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ─── Incidencias ──────────────────────────────────────────────────────────

    suspend fun crearIncidencia(
        descripcion: String,
        solicitudId: Long? = null,
        tipo: String? = null
    ): Result<Incidencia> = withContext(Dispatchers.IO) {
        val token = RetrofitClient.authToken?.let { "Bearer $it" } ?: ""
        try {
            val response = apiService.crearIncidencia(
                token = token,
                request = CrearIncidenciaRequest(
                    solicitudId = solicitudId,
                    tipo = tipo,
                    descripcion = descripcion
                )
            )
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al crear incidencia"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMisIncidencias(): List<Incidencia> = withContext(Dispatchers.IO) {
        val token = RetrofitClient.authToken?.let { "Bearer $it" } ?: ""
        try {
            val response = apiService.getMisIncidencias(token)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}
