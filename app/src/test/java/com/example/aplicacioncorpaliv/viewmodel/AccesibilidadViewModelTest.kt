package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.AccesibilidadDataStore
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AccesibilidadViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val aplicacion = mockk<Application>(relaxed = true)
    private val dataStore = mockk<AccesibilidadDataStore>()

    private fun crearViewModel(): AccesibilidadViewModel {
        every { dataStore.nivelTexto } returns flowOf(2)
        every { dataStore.altoContraste } returns flowOf(true)
        every { dataStore.lecturaVoz } returns flowOf(false)
        coEvery { dataStore.guardarNivelTexto(any()) } just Runs
        coEvery { dataStore.guardarAltoContraste(any()) } just Runs
        coEvery { dataStore.guardarLecturaVoz(any()) } just Runs
        return AccesibilidadViewModel(aplicacion, dataStore)
    }

    @Test
    fun `al crearse toma las opciones guardadas en el telefono`() {
        val viewModel = crearViewModel()

        assertEquals(2, viewModel.nivelTexto.value)
        assertTrue(viewModel.altoContraste.value)
        assertFalse(viewModel.lecturaVoz.value)
    }

    @Test
    fun `cambiar una opcion la guarda en el DataStore`() {
        val viewModel = crearViewModel()

        viewModel.cambiarNivelTexto(1)
        viewModel.cambiarAltoContraste(false)
        viewModel.cambiarLecturaVoz(true)

        coVerify { dataStore.guardarNivelTexto(1) }
        coVerify { dataStore.guardarAltoContraste(false) }
        coVerify { dataStore.guardarLecturaVoz(true) }
    }

    @Test
    fun `restablecer vuelve a los valores de fabrica`() {
        val viewModel = crearViewModel()

        viewModel.restablecer()

        coVerify { dataStore.guardarNivelTexto(0) }
        coVerify { dataStore.guardarAltoContraste(false) }
        coVerify { dataStore.guardarLecturaVoz(true) }
    }
}
