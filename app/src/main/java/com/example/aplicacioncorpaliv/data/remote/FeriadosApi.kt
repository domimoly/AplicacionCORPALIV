package com.example.aplicacioncorpaliv.data.remote

import com.example.aplicacioncorpaliv.model.Feriado
import retrofit2.http.GET
import retrofit2.http.Path

// API EXTERNA: Nager.Date. Se usa igual que las de nuestros microservicios (interfaz de Retrofit con @GET),
// pero la dirección base es de internet y no del computador. No necesita clave ni token.
interface FeriadosApi {
    // GET https://date.nager.at/api/v3/PublicHolidays/2026/CL -> los feriados de Chile del año 2026
    @GET("api/v3/PublicHolidays/{anio}/{pais}")
    suspend fun feriados(
        @Path("anio") anio: Int,
        @Path("pais") pais: String
    ): List<Feriado>
}
