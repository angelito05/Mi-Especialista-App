package com.example.miespecialista.data.remote

import com.example.miespecialista.data.model.Billetera
import com.example.miespecialista.data.model.Categoria
import com.example.miespecialista.data.model.Incidencia
import com.example.miespecialista.data.model.Mensaje
import com.example.miespecialista.data.model.Movimiento
import com.example.miespecialista.data.model.Resena
import com.example.miespecialista.data.model.Solicitud
import com.example.miespecialista.data.model.UserAccount
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// ─── DTOs de Auth ────────────────────────────────────────────────────────────

data class LoginRequest(
    @SerializedName("telefono") val telefono: String,
    @SerializedName("password") val password: String
)

data class RegisterRequest(
    @SerializedName("telefono") val telefono: String,
    @SerializedName("password") val password: String,
    @SerializedName("rol") val rol: String? = null,
    @SerializedName("foto_perfil") val fotoPerfil: String? = null
)

data class AuthResponse(
    @SerializedName("mensaje") val mensaje: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("usuario") val usuario: UserAccount? = null,
    @SerializedName("billetera") val billetera: Billetera? = null
)

// ─── DTOs de Solicitudes ─────────────────────────────────────────────────────

data class CrearSolicitudRequest(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("fecha_servicio") val fechaServicio: String? = null,
    @SerializedName("hora_servicio") val horaServicio: String? = null
)

data class ActualizarSolicitudRequest(
    @SerializedName("estado") val estado: String? = null,
    @SerializedName("titulo") val titulo: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null
)

// ─── DTOs de Movimientos ─────────────────────────────────────────────────────

data class CrearMovimientoRequest(
    @SerializedName("tipo") val tipo: String,
    @SerializedName("monto") val monto: Double,
    @SerializedName("comision") val comision: Double? = null,
    @SerializedName("solicitud_id") val solicitudId: Long? = null
)

// ─── DTOs de Mensajes ────────────────────────────────────────────────────────

data class EnviarMensajeRequest(
    @SerializedName("receptor_id") val receptorId: Long,
    @SerializedName("contenido") val contenido: String,
    @SerializedName("solicitud_id") val solicitudId: Long? = null,
    @SerializedName("tipo") val tipo: String? = null
)

// ─── DTOs de Reseñas ─────────────────────────────────────────────────────────

data class CrearResenaRequest(
    @SerializedName("solicitud_id") val solicitudId: Long? = null,
    @SerializedName("colaborador_id") val colaboradorId: Long,
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("comentario") val comentario: String? = null
)

// ─── DTOs de Incidencias ─────────────────────────────────────────────────────

data class CrearIncidenciaRequest(
    @SerializedName("solicitud_id") val solicitudId: Long? = null,
    @SerializedName("tipo") val tipo: String? = null,
    @SerializedName("descripcion") val descripcion: String
)

// ─── Respuesta genérica de la API ─────────────────────────────────────────────

data class ApiListResponse<T>(
    @SerializedName("data") val data: List<T>? = null,
    @SerializedName("mensaje") val mensaje: String? = null
)

data class ApiItemResponse<T>(
    @SerializedName("data") val data: T? = null,
    @SerializedName("mensaje") val mensaje: String? = null
)

// ─── Interfaz Retrofit de la API real ────────────────────────────────────────

interface ApiService {

    // Auth
    @POST("api/auth/registro")
    suspend fun registro(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @GET("api/auth/perfil")
    suspend fun perfil(
        @Header("Authorization") token: String
    ): Response<AuthResponse>

    // Solicitudes
    @GET("api/solicitudes")
    suspend fun getSolicitudes(): Response<List<Solicitud>>

    @POST("api/solicitudes")
    suspend fun crearSolicitud(
        @Header("Authorization") token: String,
        @Body request: CrearSolicitudRequest
    ): Response<Solicitud>

    @GET("api/solicitudes/{id}")
    suspend fun getSolicitud(
        @Path("id") id: Long
    ): Response<Solicitud>

    @PUT("api/solicitudes/{id}")
    suspend fun actualizarSolicitud(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: ActualizarSolicitudRequest
    ): Response<Solicitud>

    // Categorías
    @GET("api/categorias")
    suspend fun getCategorias(): Response<List<Categoria>>

    // Movimientos
    @POST("api/movimientos")
    suspend fun crearMovimiento(
        @Header("Authorization") token: String,
        @Body request: CrearMovimientoRequest
    ): Response<Movimiento>

    // Mensajes
    @POST("api/mensajes")
    suspend fun enviarMensaje(
        @Header("Authorization") token: String,
        @Body request: EnviarMensajeRequest
    ): Response<Mensaje>

    @GET("api/mensajes/mis_mensajes")
    suspend fun getMisMensajes(
        @Header("Authorization") token: String
    ): Response<List<Mensaje>>

    // Reseñas
    @GET("api/resenas")
    suspend fun getResenas(): Response<List<Resena>>

    @POST("api/resenas")
    suspend fun crearResena(
        @Header("Authorization") token: String,
        @Body request: CrearResenaRequest
    ): Response<Resena>

    // Incidencias
    @POST("api/incidencias")
    suspend fun crearIncidencia(
        @Header("Authorization") token: String,
        @Body request: CrearIncidenciaRequest
    ): Response<Incidencia>

    @GET("api/incidencias/mis_incidencias")
    suspend fun getMisIncidencias(
        @Header("Authorization") token: String
    ): Response<List<Incidencia>>
}
