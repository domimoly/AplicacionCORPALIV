package com.example.aplicacioncorpaliv.data.remote

import com.example.aplicacioncorpaliv.model.HistoriaImpacto
import com.example.aplicacioncorpaliv.model.IndicadorImpacto
import com.example.aplicacioncorpaliv.model.Programa
import retrofit2.http.GET

// EXPLICAR: una interfaz de Retrofit describe los endpoints del microservicio. Cada función es UNA llamada:
// @GET("programas") significa "pide a http://.../programas". 'suspend' = se ejecuta sin congelar la pantalla.
// Estos tres endpoints son públicos (no piden token) y solo devuelven lo que está PUBLICADO.
interface ContenidosApi {
    @GET("programas")
    suspend fun getProgramas(): List<Programa>

    @GET("historias")
    suspend fun getHistorias(): List<HistoriaImpacto>

    @GET("indicadores")
    suspend fun getIndicadores(): List<IndicadorImpacto>
}
