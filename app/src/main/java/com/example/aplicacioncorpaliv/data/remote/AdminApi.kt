package com.example.aplicacioncorpaliv.data.remote

import com.example.aplicacioncorpaliv.model.LoginRequest
import com.example.aplicacioncorpaliv.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

// Endpoint de ms-admin (puerto 9093): el login de los gestores. Devuelve el token JWT que se usa en las demás llamadas.
interface AdminApi {
    @POST("auth/login")
    suspend fun login(@Body datos: LoginRequest): LoginResponse
}
