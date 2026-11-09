package com.example.miespecialista.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Interceptor que inyecta automáticamente el token JWT en cada petición que lo requiera
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = RetrofitClient.authToken
        val originalRequest = chain.request()

        // Solo inyectar si el token está presente y la cabecera Authorization no existe ya
        val request = if (!token.isNullOrBlank() &&
            originalRequest.header("Authorization") == null
        ) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}

// Cliente Singleton de Retrofit con soporte para JWT y logging
object RetrofitClient {

    // ── URL de producción en Fly.io ──────────────────────────────────────────
    // Cambiar USE_PRODUCTION a true para usar el backend real en Fly.io.
    // Durante desarrollo offline, dejar en false para apuntar al emulador local.
    private const val USE_PRODUCTION = true
    private const val PRODUCTION_URL  = "https://proyecto-api.fly.dev/"
    private const val DEVELOPMENT_URL = "http://10.0.2.2:8080/"

    private val BASE_URL: String
        get() = if (USE_PRODUCTION) PRODUCTION_URL else DEVELOPMENT_URL

    // Token JWT activo — se guarda al hacer login/registro y se limpia al logout
    var authToken: String? = null

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(loggingInterceptor)
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
