package com.example.aplicacioncorpaliv.data.remote

import com.example.aplicacioncorpaliv.model.CambioEstado
import com.example.aplicacioncorpaliv.model.Seguimiento
import com.example.aplicacioncorpaliv.model.Solicitud
import com.example.aplicacioncorpaliv.model.SolicitudRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

// Endpoints de ms-vinculacion (puerto 9092).
// EXPLICAR: los dos primeros son PÚBLICOS (cualquier visitante). Los otros son para gestores y necesitan el token:
// @Header("Authorization") lo envía en la cabecera de la petición, con el formato "Bearer <token>".
// @Path reemplaza el {codigo} o el {id} de la ruta. @Body convierte un objeto Kotlin en el JSON que se envía.
interface VinculacionApi {
    @POST("solicitudes")
    suspend fun crear(@Body solicitud: SolicitudRequest): Seguimiento

    @GET("solicitudes/seguimiento/{codigo}")
    suspend fun seguimiento(@Path("codigo") codigo: String): Seguimiento

    @GET("solicitudes")
    suspend fun listar(@Header("Authorization") token: String): List<Solicitud>

    @PATCH("solicitudes/{id}/estado")
    suspend fun cambiarEstado(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body cambio: CambioEstado
    ): Solicitud

    @DELETE("solicitudes/{id}")
    suspend fun eliminar(@Header("Authorization") token: String, @Path("id") id: Long)
}
