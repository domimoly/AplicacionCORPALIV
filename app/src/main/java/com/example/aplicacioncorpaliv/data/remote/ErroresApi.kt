package com.example.aplicacioncorpaliv.data.remote

import com.google.gson.Gson
import java.io.IOException
import retrofit2.HttpException

// Forma del JSON de error que devuelven los microservicios (solo nos interesa el mensaje)
private data class ErrorApi(val message: String? = null)

// EXPLICAR: convierte un error técnico en una frase que se le puede mostrar a la persona.
// HttpException = el servidor respondió, pero con un error (400, 401, 404, 503...).
// IOException = ni siquiera se pudo llegar al servidor (apagado, sin internet o demasiado lento).
fun mensajeDeError(e: Exception): String = when (e) {
    is HttpException -> when (e.code()) {
        400 -> mensajeDelServidor(e) ?: "Revisa los datos ingresados."
        401 -> "Tu sesión expiró o no tienes permiso. Inicia sesión de nuevo."
        404 -> "No se encontró lo que se buscaba."
        503 -> mensajeDelServidor(e) ?: "Un servicio del servidor no está disponible por ahora."
        else -> "El servidor respondió con un error (${e.code()})."
    }
    is IOException -> "No hay conexión con el servidor. Revisa que esté encendido y que tengas internet."
    else -> "Ocurrió un error inesperado."
}

// Lee el campo 'message' del JSON de error (por ejemplo: "No se puede modificar una solicitud CONCRETADO")
private fun mensajeDelServidor(e: HttpException): String? {
    return try {
        val cuerpo = e.response()?.errorBody()?.string()
        Gson().fromJson(cuerpo, ErrorApi::class.java)?.message
    } catch (ex: Exception) {
        null
    }
}
