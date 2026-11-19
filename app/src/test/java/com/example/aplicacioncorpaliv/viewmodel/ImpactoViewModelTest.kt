package com.example.aplicacioncorpaliv.viewmodel

import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.ContenidosRepository
import com.example.aplicacioncorpaliv.data.DatosDeEjemplo
import com.example.aplicacioncorpaliv.model.HistoriaImpacto
import com.example.aplicacioncorpaliv.model.IndicadorImpacto
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ImpactoViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val repositorio = mockk<ContenidosRepository>()
    private val indicadores = listOf(IndicadorImpacto(1, "Personas atendidas", 120.0, "personas", "2026"))
    private val historias = listOf(HistoriaImpacto(1, "Historia de prueba", "Resumen", "Contenido"))

    @Test
    fun `al crearse carga indicadores e historias del servidor`() {
        coEvery { repositorio.getIndicadores() } returns indicadores
        coEvery { repositorio.getHistorias() } returns historias

        val viewModel = ImpactoViewModel(repositorio)

        assertEquals(indicadores, viewModel.indicadores.value)
        assertEquals(historias, viewModel.historias.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun `si el servidor falla muestra datos de ejemplo y un mensaje`() {
        coEvery { repositorio.getIndicadores() } throws IOException("sin conexión")

        val viewModel = ImpactoViewModel(repositorio)

        assertEquals(DatosDeEjemplo.indicadores, viewModel.indicadores.value)
        assertEquals(DatosDeEjemplo.historias, viewModel.historias.value)
        assertNotNull(viewModel.error.value)
    }
}
