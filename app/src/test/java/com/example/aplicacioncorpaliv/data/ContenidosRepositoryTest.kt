package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.ContenidosApi
import com.example.aplicacioncorpaliv.model.HistoriaImpacto
import com.example.aplicacioncorpaliv.model.IndicadorImpacto
import com.example.aplicacioncorpaliv.model.Programa
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ContenidosRepositoryTest {

    private val api = mockk<ContenidosApi>()
    private val repositorio = ContenidosRepository(api)

    @Test
    fun `getProgramas entrega lo que responde el servidor`() = runTest {
        val programas = listOf(Programa(1, "Programa Prueba", "Resumen", "Descripción", "Educación"))
        coEvery { api.getProgramas() } returns programas

        assertEquals(programas, repositorio.getProgramas())
    }

    @Test
    fun `getHistorias entrega lo que responde el servidor`() = runTest {
        val historias = listOf(HistoriaImpacto(1, "Historia de prueba", "Resumen", "Contenido"))
        coEvery { api.getHistorias() } returns historias

        assertEquals(historias, repositorio.getHistorias())
    }

    @Test
    fun `getIndicadores entrega lo que responde el servidor`() = runTest {
        val indicadores = listOf(IndicadorImpacto(1, "Personas atendidas", 120.0, "personas", "2026"))
        coEvery { api.getIndicadores() } returns indicadores

        assertEquals(indicadores, repositorio.getIndicadores())
    }
}
