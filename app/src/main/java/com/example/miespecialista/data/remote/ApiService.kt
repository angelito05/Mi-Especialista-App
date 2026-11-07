package com.example.miespecialista.data.remote

import com.example.miespecialista.data.model.ServiceJob
import com.example.miespecialista.data.model.SpecialistData
import com.example.miespecialista.data.model.UserAccount
import com.example.miespecialista.data.model.WalletTransaction
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// DTOs para peticiones y respuestas de la API REST
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val address: String? = null
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String?,
    val data: T?
)

// Interfaz Retrofit de conexión a la API de la Base de Datos Remota
interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<UserAccount>>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<UserAccount>>

    @PUT("api/user/profile")
    suspend fun updateProfile(@Body user: UserAccount): Response<ApiResponse<UserAccount>>

    @GET("api/specialists")
    suspend fun getSpecialists(@Query("query") query: String? = null): Response<ApiResponse<List<SpecialistData>>>

    @GET("api/services")
    suspend fun getServices(): Response<ApiResponse<List<ServiceJob>>>

    @GET("api/wallet/transactions")
    suspend fun getTransactions(): Response<ApiResponse<List<WalletTransaction>>>
}
