package com.example.miespecialista.data.model

import com.google.gson.annotations.SerializedName

// Modelo real del backend para la cuenta de usuario con soporte y compatibilidad para la UI
data class UserAccount(
    @SerializedName("usuario_id") val usuarioId: Long? = null,
    @SerializedName("telefono") val telefono: String = "",
    @SerializedName("rol") val rol: String = "cliente",
    @SerializedName("foto_perfil") val fotoPerfil: String? = null,
    @SerializedName("estado_cuenta") val estadoCuenta: String = "activo",
    @SerializedName("fecha_registro") val fechaRegistro: String? = null,
    // Campos locales y de UI (compatibilidad con vistas de Android y perfil)
    val password: String = "",
    val fullName: String = if (telefono.isNotBlank()) "Usuario ($telefono)" else "Usuario",
    val email: String = "",
    val phone: String = telefono,
    val address: String = "Av. Principal #45"
) {
    // Constructor de compatibilidad para vistas o previews que usan fullName/email/password
    constructor(
        fullName: String,
        email: String,
        password: String,
        phone: String = "+52 55 1234 5678",
        address: String = "Av. Principal #45, Depto 201"
    ) : this(
        usuarioId = 1L,
        telefono = phone,
        rol = "cliente",
        fotoPerfil = null,
        estadoCuenta = "activo",
        fechaRegistro = null,
        password = password,
        fullName = fullName,
        email = email,
        phone = phone,
        address = address
    )
}

// Modelo real del backend para Billetera
data class Billetera(
    @SerializedName("billetera_id") val billeteraId: Long? = null,
    @SerializedName("usuario_id") val usuarioId: Long? = null,
    @SerializedName("saldo") val saldo: Double = 0.0,
    @SerializedName("moneda") val moneda: String = "USD",
    @SerializedName("estado") val estado: String = "activo",
    @SerializedName("fecha_creacion") val fechaCreacion: String? = null
)

// Modelo real del backend para Solicitudes de servicio
data class Solicitud(
    @SerializedName("solicitud_id") val solicitudId: Long? = null,
    @SerializedName("usuario_id") val usuarioId: Long? = null,
    @SerializedName("titulo") val titulo: String = "",
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("direccion") val direccion: String? = null,
    @SerializedName("fecha_servicio") val fechaServicio: String? = null,
    @SerializedName("hora_servicio") val horaServicio: String? = null,
    @SerializedName("estado") val estado: String = "pendiente",
    @SerializedName("fecha_solicitud") val fechaSolicitud: String? = null
)

// Modelo real del backend para Categorías
data class Categoria(
    @SerializedName("categoria_id") val categoriaId: Long? = null,
    @SerializedName("nombre") val nombre: String = "",
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("icono") val icono: String? = null,
    @SerializedName("activo") val activo: Boolean = true
)

// Modelo real del backend para Movimientos de billetera
data class Movimiento(
    @SerializedName("movimiento_id") val movimientoId: Long? = null,
    @SerializedName("usuario_id") val usuarioId: Long? = null,
    @SerializedName("solicitud_id") val solicitudId: Long? = null,
    @SerializedName("tipo") val tipo: String = "",
    @SerializedName("monto") val monto: Double = 0.0,
    @SerializedName("comision") val comision: Double? = null,
    @SerializedName("monto_neto") val montoNeto: Double? = null,
    @SerializedName("estado") val estado: String = "completado",
    @SerializedName("fecha") val fecha: String? = null
)

// Modelo real del backend para Mensajes
data class Mensaje(
    @SerializedName("mensaje_id") val mensajeId: Long? = null,
    @SerializedName("solicitud_id") val solicitudId: Long? = null,
    @SerializedName("emisor_id") val emisorId: Long? = null,
    @SerializedName("receptor_id") val receptorId: Long? = null,
    @SerializedName("tipo") val tipo: String? = "texto",
    @SerializedName("contenido") val contenido: String = "",
    @SerializedName("archivo_url") val archivoUrl: String? = null,
    @SerializedName("leido") val leido: Boolean = false,
    @SerializedName("fecha_envio") val fechaEnvio: String? = null
)

// Modelo real del backend para Reseñas
data class Resena(
    @SerializedName("resena_id") val resenaId: Long? = null,
    @SerializedName("solicitud_id") val solicitudId: Long? = null,
    @SerializedName("cliente_id") val clienteId: Long? = null,
    @SerializedName("colaborador_id") val colaboradorId: Long = 0,
    @SerializedName("calificacion") val calificacion: Int = 5,
    @SerializedName("comentario") val comentario: String? = null,
    @SerializedName("fecha") val fecha: String? = null
)

// Modelo real del backend para Incidencias
data class Incidencia(
    @SerializedName("incidencia_id") val incidenciaId: Long? = null,
    @SerializedName("usuario_id") val usuarioId: Long? = null,
    @SerializedName("solicitud_id") val solicitudId: Long? = null,
    @SerializedName("soporte_id") val soporteId: Long? = null,
    @SerializedName("tipo") val tipo: String? = "retraso",
    @SerializedName("descripcion") val descripcion: String = "",
    @SerializedName("estado") val estado: String? = "abierto",
    @SerializedName("fecha_reporte") val fechaReporte: String? = null
)

// Estado de validación de los requisitos de contraseña
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
