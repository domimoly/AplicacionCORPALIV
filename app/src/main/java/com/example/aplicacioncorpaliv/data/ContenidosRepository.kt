package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.ContenidosApi
import com.example.aplicacioncorpaliv.data.remote.RetrofitInstance
import com.example.aplicacioncorpaliv.model.HistoriaImpacto
import com.example.aplicacioncorpaliv.model.IndicadorImpacto
import com.example.aplicacioncorpaliv.model.Programa

// REPOSITORIO: el intermediario entre los ViewModel y el microservicio ms-contenidos (igual que PostRepository del ejemplo).
// EXPLICAR: el ViewModel no sabe de Retrofit; solo le pide los datos a este repositorio.
// La 'api' se recibe por parámetro (con un valor por defecto) para poder reemplazarla por una falsa en los tests unitarios.
class ContenidosRepository(
    private val api: ContenidosApi = RetrofitInstance.contenidos
) {
    suspend fun getProgramas(): List<Programa> = api.getProgramas()

    suspend fun getHistorias(): List<HistoriaImpacto> = api.getHistorias()

    suspend fun getIndicadores(): List<IndicadorImpacto> = api.getIndicadores()
}
