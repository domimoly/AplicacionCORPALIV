package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import android.net.Uri
import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.PerfilDataStore
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class PerfilViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val aplicacion = mockk<Application>(relaxed = true)
    private val dataStore = mockk<PerfilDataStore>()

    private fun crearViewModel(nombre: String = "", contacto: String = ""): PerfilViewModel {
        every { dataStore.nombre } returns flowOf(nombre)
        every { dataStore.medioContacto } returns flowOf(contacto)
        return PerfilViewModel(aplicacion, dataStore)
    }

    @Test
    fun `al crearse trae lo guardado en el telefono`() {
        val viewModel = crearViewModel("Ana Prueba", "ana@ejemplo.cl")

        assertEquals("Ana Prueba", viewModel.nombre.value)
        assertEquals("ana@ejemplo.cl", viewModel.medioContacto.value)
    }

    @Test
    fun `actualizar cambia el nombre y el contacto`() {
        val viewModel = crearViewModel()

        viewModel.actualizarNombre("Luis Prueba")
        viewModel.actualizarMedioContacto("luis@ejemplo.cl")

        assertEquals("Luis Prueba", viewModel.nombre.value)
        assertEquals("luis@ejemplo.cl", viewModel.medioContacto.value)
    }

    @Test
    fun `la imagen de la galeria se guarda y un null se ignora`() {
        val viewModel = crearViewModel()
        val uri = mockk<Uri>()

        viewModel.actualizarImagenDesdeGaleria(null)
        assertNull(viewModel.imagenUri.value)

        viewModel.actualizarImagenDesdeGaleria(uri)
        assertSame(uri, viewModel.imagenUri.value)
    }

    @Test
    fun `la imagen de la camara solo se guarda si la captura fue correcta`() {
        val viewModel = crearViewModel()
        val uri = mockk<Uri>()

        viewModel.actualizarImagenDesdeCamara(uri, capturaCorrecta = false)
        assertNull(viewModel.imagenUri.value)

        viewModel.actualizarImagenDesdeCamara(null, capturaCorrecta = true)
        assertNull(viewModel.imagenUri.value)

        viewModel.actualizarImagenDesdeCamara(uri, capturaCorrecta = true)
        assertSame(uri, viewModel.imagenUri.value)
    }

    @Test
    fun `guardarDatos con datos validos los guarda sin espacios sobrantes`() {
        coEvery { dataStore.guardar("Ana Prueba", "ana@ejemplo.cl") } just Runs
        val viewModel = crearViewModel()
        viewModel.actualizarNombre("  Ana Prueba ")
        viewModel.actualizarMedioContacto(" ana@ejemplo.cl ")

        viewModel.guardarDatos()

        coVerify { dataStore.guardar("Ana Prueba", "ana@ejemplo.cl") }
        assertTrue(viewModel.guardadoCorrectamente.value)
        assertNull(viewModel.errores.value.nombre)
    }

    @Test
    fun `guardarDatos acepta los dos campos vacios porque son opcionales`() {
        coEvery { dataStore.guardar("", "") } just Runs
        val viewModel = crearViewModel()

        viewModel.guardarDatos()

        coVerify { dataStore.guardar("", "") }
        assertTrue(viewModel.guardadoCorrectamente.value)
    }

    @Test
    fun `guardarDatos con datos invalidos muestra errores y no guarda`() {
        val viewModel = crearViewModel()
        viewModel.actualizarNombre("ab")
        viewModel.actualizarMedioContacto("esto no es un contacto")

        viewModel.guardarDatos()

        assertNotNull(viewModel.errores.value.nombre)
        assertNotNull(viewModel.errores.value.medioContacto)
        assertFalse(viewModel.guardadoCorrectamente.value)
        coVerify(exactly = 0) { dataStore.guardar(any(), any()) }
    }

    @Test
    fun `ocultarMensajeGuardado apaga el aviso`() {
        coEvery { dataStore.guardar(any(), any()) } just Runs
        val viewModel = crearViewModel()
        viewModel.guardarDatos()

        viewModel.ocultarMensajeGuardado()

        assertFalse(viewModel.guardadoCorrectamente.value)
    }
}
