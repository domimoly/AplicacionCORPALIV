package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.Contacto
import com.example.aplicacioncorpaliv.data.ContactoRepository
import com.example.aplicacioncorpaliv.data.RegistroRepository
import com.example.aplicacioncorpaliv.errorHttp
import com.example.aplicacioncorpaliv.registroDePrueba
import com.example.aplicacioncorpaliv.seguimientoDePrueba
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RegistrosViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val aplicacion = mockk<Application>(relaxed = true)
    private val repositorio = mockk<RegistroRepository>()
    private val repositorioContactos = mockk<ContactoRepository>()
    private val contacto = Contacto(id = 1, nombre = "Ana Prueba", correo = "ana@ejemplo.cl", mensaje = "Hola")

    private fun crearViewModel(): RegistrosViewModel {
        every { repositorio.registros } returns flowOf(listOf(registroDePrueba()))
        every { repositorioContactos.contactos } returns flowOf(listOf(contacto))
        return RegistrosViewModel(aplicacion, repositorio, repositorioContactos)
    }

    @Test
    fun `entrega las solicitudes y los mensajes guardados en el telefono`() = runTest {
        val viewModel = crearViewModel()

        assertEquals(1, viewModel.registros.first().size)
        assertEquals(listOf(contacto), viewModel.contactos.first())
    }

    @Test
    fun `un codigo con formato incorrecto no consulta al servidor`() {
        val viewModel = crearViewModel()

        viewModel.consultarPorCodigo("ABC")

        assertEquals(ResultadoConsulta.CodigoInvalido, viewModel.consulta.value)
        coVerify(exactly = 0) { repositorio.consultarSeguimiento(any()) }
    }

    @Test
    fun `un codigo existente entrega el seguimiento`() {
        val seguimiento = seguimientoDePrueba("EN_REVISION", "Te llamaremos")
        coEvery { repositorio.consultarSeguimiento("COL-AAAA2222") } returns seguimiento
        val viewModel = crearViewModel()

        viewModel.consultarPorCodigo("COL-AAAA2222")

        assertEquals(ResultadoConsulta.Encontrada(seguimiento), viewModel.consulta.value)
    }

    @Test
    fun `un codigo que el servidor no conoce da NoEncontrada`() {
        coEvery { repositorio.consultarSeguimiento(any()) } throws errorHttp(404)
        val viewModel = crearViewModel()

        viewModel.consultarPorCodigo("COL-AAAA2222")

        assertEquals(ResultadoConsulta.NoEncontrada, viewModel.consulta.value)
    }

    @Test
    fun `otro error del servidor da un mensaje de error`() {
        coEvery { repositorio.consultarSeguimiento(any()) } throws errorHttp(500)
        val viewModel = crearViewModel()

        viewModel.consultarPorCodigo("COL-AAAA2222")

        assertEquals(
            ResultadoConsulta.Error("El servidor respondió con un error (500)."),
            viewModel.consulta.value
        )
    }

    @Test
    fun `sin conexion la consulta da un mensaje de error`() {
        coEvery { repositorio.consultarSeguimiento(any()) } throws IOException("sin conexión")
        val viewModel = crearViewModel()

        viewModel.consultarPorCodigo("COL-AAAA2222")

        val resultado = viewModel.consulta.value as ResultadoConsulta.Error
        assertTrue(resultado.mensaje.startsWith("No hay conexión"))
    }

    @Test
    fun `limpiarConsulta vuelve al estado inicial`() {
        val viewModel = crearViewModel()
        viewModel.consultarPorCodigo("ABC")

        viewModel.limpiarConsulta()

        assertEquals(ResultadoConsulta.SinConsultar, viewModel.consulta.value)
    }

    @Test
    fun `actualizar correcto no deja avisos`() {
        coEvery { repositorio.actualizarEstados() } just Runs
        val viewModel = crearViewModel()

        viewModel.actualizar()

        assertNull(viewModel.aviso.value)
        assertFalse(viewModel.actualizando.value)
    }

    @Test
    fun `actualizar sin conexion deja un aviso`() {
        coEvery { repositorio.actualizarEstados() } throws IOException("sin conexión")
        val viewModel = crearViewModel()

        viewModel.actualizar()

        assertTrue(viewModel.aviso.value!!.startsWith("No pudimos actualizar los estados."))
        assertFalse(viewModel.actualizando.value)
    }

    @Test
    fun `eliminar y eliminarContacto llaman a sus repositorios`() {
        val registro = registroDePrueba()
        coEvery { repositorio.delete(registro) } just Runs
        coEvery { repositorioContactos.delete(contacto) } just Runs
        val viewModel = crearViewModel()

        viewModel.eliminar(registro)
        viewModel.eliminarContacto(contacto)

        coVerify { repositorio.delete(registro) }
        coVerify { repositorioContactos.delete(contacto) }
    }
}
