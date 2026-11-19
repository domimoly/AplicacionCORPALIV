package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.ContactoRepository
import com.example.aplicacioncorpaliv.data.PerfilDataStore
import com.example.aplicacioncorpaliv.model.ContactoModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ContactoViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val aplicacion = mockk<Application>(relaxed = true)
    private val repositorio = mockk<ContactoRepository>()
    private val perfil = mockk<PerfilDataStore>()

    private fun crearViewModel() = ContactoViewModel(aplicacion, repositorio, perfil)

    private fun llenarFormulario(viewModel: ContactoViewModel) {
        viewModel.actualizarNombre("Ana Prueba")
        viewModel.actualizarCorreo("ana@ejemplo.cl")
        viewModel.actualizarMensaje("Tengo una consulta")
        viewModel.actualizarAceptaPrivacidad(true)
    }

    @Test
    fun `los metodos actualizar cambian cada campo`() {
        val viewModel = crearViewModel()

        llenarFormulario(viewModel)

        assertEquals(ContactoModel("Ana Prueba", "ana@ejemplo.cl", "Tengo una consulta", true), viewModel.formulario.value)
    }

    @Test
    fun `un formulario vacio no es valido`() {
        val viewModel = crearViewModel()

        val valido = viewModel.validarFormulario()

        val errores = viewModel.errores.value
        assertFalse(valido)
        assertEquals("El nombre es obligatorio", errores.nombre)
        assertEquals("El correo es obligatorio", errores.correo)
        assertEquals("El mensaje es obligatorio", errores.mensaje)
        assertNotNull(errores.privacidad)
    }

    @Test
    fun `un correo mal escrito se marca como error`() {
        val viewModel = crearViewModel()
        llenarFormulario(viewModel)
        viewModel.actualizarCorreo("ana@ejemplo")

        assertFalse(viewModel.validarFormulario())
        assertEquals("El correo no tiene un formato válido", viewModel.errores.value.correo)
    }

    @Test
    fun `guardar con datos invalidos no llama al servidor`() {
        val viewModel = crearViewModel()

        assertFalse(viewModel.guardar())

        coVerify(exactly = 0) { repositorio.save(any()) }
    }

    @Test
    fun `guardar correcto vacia el formulario y avisa`() {
        coEvery { repositorio.save(any()) } returns Unit
        val viewModel = crearViewModel()
        llenarFormulario(viewModel)

        assertTrue(viewModel.guardar())

        assertTrue(viewModel.guardadoCorrectamente.value)
        assertEquals(ContactoModel(), viewModel.formulario.value)
        assertNull(viewModel.errorEnvio.value)
        assertFalse(viewModel.enviando.value)
    }

    @Test
    fun `si el servidor falla guarda el error y conserva lo escrito`() {
        coEvery { repositorio.save(any()) } throws IOException("sin conexión")
        val viewModel = crearViewModel()
        llenarFormulario(viewModel)

        viewModel.guardar()

        assertTrue(viewModel.errorEnvio.value!!.startsWith("No hay conexión"))
        assertFalse(viewModel.guardadoCorrectamente.value)
        assertEquals("Ana Prueba", viewModel.formulario.value.nombre)
    }

    @Test
    fun `cerrarErrorEnvio y ocultarMensajeGuardado limpian los avisos`() {
        coEvery { repositorio.save(any()) } throws IOException("sin conexión")
        val viewModel = crearViewModel()
        llenarFormulario(viewModel)
        viewModel.guardar()

        viewModel.cerrarErrorEnvio()
        viewModel.ocultarMensajeGuardado()

        assertNull(viewModel.errorEnvio.value)
        assertFalse(viewModel.guardadoCorrectamente.value)
    }

    @Test
    fun `precargarDesdePerfil usa el contacto del perfil solo si es un correo`() {
        every { perfil.nombre } returns flowOf("Nombre del perfil")
        every { perfil.medioContacto } returns flowOf("perfil@ejemplo.cl")
        val viewModel = crearViewModel()

        viewModel.precargarDesdePerfil()

        assertEquals("Nombre del perfil", viewModel.formulario.value.nombre)
        assertEquals("perfil@ejemplo.cl", viewModel.formulario.value.correo)
    }

    @Test
    fun `precargarDesdePerfil no copia un telefono en el campo de correo`() {
        every { perfil.nombre } returns flowOf("Nombre del perfil")
        every { perfil.medioContacto } returns flowOf("+56 9 1234 5678")
        val viewModel = crearViewModel()

        viewModel.precargarDesdePerfil()

        assertEquals("", viewModel.formulario.value.correo)
    }
}
