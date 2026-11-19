package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.EstadoDataStore
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// EXPLICAR: este ViewModel espera 1 segundo (delay) antes de leer la preferencia. En el test no se espera de verdad:
// 'advanceUntilIdle()' adelanta el reloj de prueba para que ese segundo pase al instante.
@OptIn(ExperimentalCoroutinesApi::class)
class EstadoViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val aplicacion = mockk<Application>(relaxed = true)
    private val dataStore = mockk<EstadoDataStore>()

    @Test
    fun `mientras espera esta cargando y despues entrega la preferencia guardada`() {
        every { dataStore.estadoActivado } returns flowOf(true)
        val viewModel = EstadoViewModel(aplicacion, dataStore)
        assertTrue(viewModel.cargando.value)

        reglaMain.dispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.cargando.value)
        assertTrue(viewModel.estadoActivado.value)
        assertNull(viewModel.mensajeError.value)
    }

    @Test
    fun `si no puede leer la preferencia muestra un error`() {
        every { dataStore.estadoActivado } returns flow { throw IOException("fallo de lectura") }
        val viewModel = EstadoViewModel(aplicacion, dataStore)

        reglaMain.dispatcher.scheduler.advanceUntilIdle()

        assertEquals("No se pudo recuperar la preferencia guardada.", viewModel.mensajeError.value)
        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun `guardarEstado guarda la preferencia y avisa`() {
        every { dataStore.estadoActivado } returns flowOf(false)
        coEvery { dataStore.guardarEstado(true) } just Runs
        val viewModel = EstadoViewModel(aplicacion, dataStore)
        reglaMain.dispatcher.scheduler.advanceUntilIdle()

        viewModel.guardarEstado(true)

        coVerify { dataStore.guardarEstado(true) }
        assertTrue(viewModel.estadoActivado.value)
        assertTrue(viewModel.guardadoCorrectamente.value)
    }

    @Test
    fun `si no puede guardar muestra un error`() {
        every { dataStore.estadoActivado } returns flowOf(false)
        coEvery { dataStore.guardarEstado(any()) } throws IOException("fallo de escritura")
        val viewModel = EstadoViewModel(aplicacion, dataStore)
        reglaMain.dispatcher.scheduler.advanceUntilIdle()

        viewModel.guardarEstado(true)

        assertEquals("No se pudo guardar la preferencia.", viewModel.mensajeError.value)
        assertFalse(viewModel.guardadoCorrectamente.value)
    }

    @Test
    fun `ocultarMensajeGuardado apaga el aviso`() {
        every { dataStore.estadoActivado } returns flowOf(false)
        coEvery { dataStore.guardarEstado(any()) } just Runs
        val viewModel = EstadoViewModel(aplicacion, dataStore)
        viewModel.guardarEstado(true)

        viewModel.ocultarMensajeGuardado()

        assertFalse(viewModel.guardadoCorrectamente.value)
    }
}
