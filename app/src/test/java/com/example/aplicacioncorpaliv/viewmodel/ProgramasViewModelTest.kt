package com.example.aplicacioncorpaliv.viewmodel

import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.ContenidosRepository
import com.example.aplicacioncorpaliv.data.DatosDeEjemplo
import com.example.aplicacioncorpaliv.model.Programa
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ProgramasViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val repositorio = mockk<ContenidosRepository>()
    private val programas = listOf(Programa(1, "Programa Prueba", "Resumen", "Descripción", "Educación"))

    @Test
    fun `al crearse carga los programas del servidor`() {
        coEvery { repositorio.getProgramas() } returns programas

        val viewModel = ProgramasViewModel(repositorio)

        assertEquals(programas, viewModel.programas.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun `si el servidor falla muestra los datos de ejemplo y un mensaje`() {
        coEvery { repositorio.getProgramas() } throws IOException("sin conexión")

        val viewModel = ProgramasViewModel(repositorio)

        assertEquals(DatosDeEjemplo.programas, viewModel.programas.value)
        assertNotNull(viewModel.error.value)
        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun `reintentar borra el error cuando el servidor vuelve`() {
        coEvery { repositorio.getProgramas() } throws IOException("sin conexión")
        val viewModel = ProgramasViewModel(repositorio)
        assertNotNull(viewModel.error.value)

        coEvery { repositorio.getProgramas() } returns programas
        viewModel.cargar()

        assertNull(viewModel.error.value)
        assertEquals(programas, viewModel.programas.value)
    }
}
