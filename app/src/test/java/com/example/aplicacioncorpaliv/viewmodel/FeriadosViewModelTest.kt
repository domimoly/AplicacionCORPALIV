package com.example.aplicacioncorpaliv.viewmodel

import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.FeriadosRepository
import com.example.aplicacioncorpaliv.model.Feriado
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class FeriadosViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val repositorio = mockk<FeriadosRepository>()
    private val feriados = listOf(Feriado("2026-12-08", "Inmaculada Concepción", "Immaculate Conception"))

    @Test
    fun `al crearse carga los proximos feriados`() {
        coEvery { repositorio.proximos(any(), any()) } returns feriados

        val viewModel = FeriadosViewModel(repositorio)

        assertEquals(feriados, viewModel.feriados.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun `si la API externa falla solo guarda un mensaje y no hay feriados`() {
        coEvery { repositorio.proximos(any(), any()) } throws IOException("sin conexión")

        val viewModel = FeriadosViewModel(repositorio)

        assertNull(viewModel.feriados.value)
        assertEquals(
            "No pudimos obtener los feriados. Revisa tu conexión a internet.",
            viewModel.error.value
        )
        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun `reintentar carga los feriados cuando vuelve la conexion`() {
        coEvery { repositorio.proximos(any(), any()) } throws IOException("sin conexión")
        val viewModel = FeriadosViewModel(repositorio)

        coEvery { repositorio.proximos(any(), any()) } returns feriados
        viewModel.cargar()

        assertNull(viewModel.error.value)
        assertEquals(feriados, viewModel.feriados.value)
    }
}
