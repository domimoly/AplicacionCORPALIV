package com.example.aplicacioncorpaliv.viewmodel

import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.GestorRepository
import com.example.aplicacioncorpaliv.errorHttp
import com.example.aplicacioncorpaliv.model.LoginResponse
import com.example.aplicacioncorpaliv.solicitudDePrueba
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class GestorViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val repositorio = mockk<GestorRepository>()
    private val respuestaLogin = LoginResponse("token-falso", "Gestor Prueba", "gestor@ejemplo.cl", "GESTOR")

    // Crea el ViewModel con la sesión ya iniciada y 2 solicitudes cargadas
    private fun viewModelConSesion(): GestorViewModel {
        coEvery { repositorio.iniciarSesion("gestor@ejemplo.cl", "Clave123") } returns respuestaLogin
        coEvery { repositorio.listar("token-falso") } returns listOf(solicitudDePrueba(1), solicitudDePrueba(2))
        val viewModel = GestorViewModel(repositorio)
        viewModel.iniciarSesion("gestor@ejemplo.cl", "Clave123")
        return viewModel
    }

    @Test
    fun `no inicia sesion si el correo o la clave estan vacios`() {
        val viewModel = GestorViewModel(repositorio)

        viewModel.iniciarSesion("", "")

        assertEquals("Ingresa tu correo y tu clave", viewModel.errorLogin.value)
        assertNull(viewModel.gestorActual.value)
    }

    @Test
    fun `no inicia sesion si el correo no tiene arroba`() {
        val viewModel = GestorViewModel(repositorio)

        viewModel.iniciarSesion("gestor.ejemplo.cl", "Clave123")

        assertEquals("Ingresa un correo válido", viewModel.errorLogin.value)
    }

    @Test
    fun `inicio de sesion correcto guarda el nombre y carga las solicitudes`() {
        val viewModel = viewModelConSesion()

        assertEquals("Gestor Prueba", viewModel.gestorActual.value)
        assertNull(viewModel.errorLogin.value)
        assertEquals(2, viewModel.solicitudes.value.size)
        assertFalse(viewModel.iniciando.value)
    }

    @Test
    fun `clave incorrecta muestra correo o clave incorrectos`() {
        coEvery { repositorio.iniciarSesion(any(), any()) } throws errorHttp(401)
        val viewModel = GestorViewModel(repositorio)

        viewModel.iniciarSesion("gestor@ejemplo.cl", "mala")

        assertEquals("Correo o clave incorrectos", viewModel.errorLogin.value)
        assertNull(viewModel.gestorActual.value)
        assertFalse(viewModel.iniciando.value)
    }

    @Test
    fun `otro error del servidor en el login muestra su mensaje`() {
        coEvery { repositorio.iniciarSesion(any(), any()) } throws errorHttp(500)
        val viewModel = GestorViewModel(repositorio)

        viewModel.iniciarSesion("gestor@ejemplo.cl", "Clave123")

        assertEquals("El servidor respondió con un error (500).", viewModel.errorLogin.value)
    }

    @Test
    fun `sin conexion el login muestra el aviso de conexion`() {
        coEvery { repositorio.iniciarSesion(any(), any()) } throws IOException("sin conexión")
        val viewModel = GestorViewModel(repositorio)

        viewModel.iniciarSesion("gestor@ejemplo.cl", "Clave123")

        assertTrue(viewModel.errorLogin.value!!.startsWith("No hay conexión"))
    }

    @Test
    fun `cerrar sesion vacia la sesion y la lista`() {
        val viewModel = viewModelConSesion()

        viewModel.cerrarSesion()

        assertNull(viewModel.gestorActual.value)
        assertTrue(viewModel.solicitudes.value.isEmpty())
    }

    @Test
    fun `cargar sin sesion no llama al servidor`() {
        val viewModel = GestorViewModel(repositorio)

        viewModel.cargar()

        coVerify(exactly = 0) { repositorio.listar(any()) }
    }

    @Test
    fun `si el token vencio al cargar se cierra la sesion`() {
        val viewModel = viewModelConSesion()
        coEvery { repositorio.listar("token-falso") } throws errorHttp(401)

        viewModel.cargar()

        assertNull(viewModel.gestorActual.value)
        assertEquals(
            "Tu sesión expiró o no tienes permiso. Inicia sesión de nuevo.",
            viewModel.errorLogin.value
        )
    }

    @Test
    fun `un error al cargar deja un aviso`() {
        val viewModel = viewModelConSesion()
        coEvery { repositorio.listar("token-falso") } throws IOException("sin conexión")

        viewModel.cargar()

        assertTrue(viewModel.aviso.value!!.startsWith("No hay conexión"))
        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun `cambiarEstado reemplaza la solicitud en la lista y avisa`() {
        val viewModel = viewModelConSesion()
        coEvery {
            repositorio.cambiarEstado("token-falso", 1, "EN_REVISION", "Hola")
        } returns solicitudDePrueba(1, "EN_REVISION")

        viewModel.cambiarEstado(solicitudDePrueba(1), "EN_REVISION", "Hola")

        assertEquals("EN_REVISION", viewModel.solicitudes.value.first { it.id == 1 }.estado)
        assertEquals("RECIBIDA", viewModel.solicitudes.value.first { it.id == 2 }.estado)
        assertEquals("Estado actualizado: En revisión.", viewModel.aviso.value)
    }

    @Test
    fun `cambiarEstado muestra el mensaje del servidor si lo rechaza`() {
        val viewModel = viewModelConSesion()
        coEvery { repositorio.cambiarEstado(any(), any(), any(), any()) } throws
            errorHttp(400, """{"message":"No se puede modificar una solicitud CONCRETADO"}""")

        viewModel.cambiarEstado(solicitudDePrueba(1), "RECIBIDA", "")

        assertEquals("No se puede modificar una solicitud CONCRETADO", viewModel.aviso.value)
        assertEquals("RECIBIDA", viewModel.solicitudes.value.first { it.id == 1 }.estado)
    }

    @Test
    fun `cambiarEstado sin sesion no llama al servidor`() {
        val viewModel = GestorViewModel(repositorio)

        viewModel.cambiarEstado(solicitudDePrueba(1), "EN_REVISION", "")

        coVerify(exactly = 0) { repositorio.cambiarEstado(any(), any(), any(), any()) }
    }

    @Test
    fun `eliminar quita la solicitud de la lista y avisa`() {
        val viewModel = viewModelConSesion()
        coEvery { repositorio.eliminar("token-falso", 1) } returns Unit

        viewModel.eliminar(solicitudDePrueba(1))

        assertEquals(listOf(2), viewModel.solicitudes.value.map { it.id })
        assertEquals("Solicitud eliminada.", viewModel.aviso.value)
    }

    @Test
    fun `eliminar con error deja la lista igual y avisa`() {
        val viewModel = viewModelConSesion()
        coEvery { repositorio.eliminar(any(), any()) } throws errorHttp(404)

        viewModel.eliminar(solicitudDePrueba(1))

        assertEquals(2, viewModel.solicitudes.value.size)
        assertEquals("No se encontró lo que se buscaba.", viewModel.aviso.value)
    }

    @Test
    fun `eliminar sin sesion no llama al servidor`() {
        val viewModel = GestorViewModel(repositorio)

        viewModel.eliminar(solicitudDePrueba(1))

        coVerify(exactly = 0) { repositorio.eliminar(any(), any()) }
    }

    @Test
    fun `limpiarAviso borra el mensaje`() {
        val viewModel = viewModelConSesion()
        coEvery { repositorio.eliminar(any(), any()) } returns Unit
        viewModel.eliminar(solicitudDePrueba(1))

        viewModel.limpiarAviso()

        assertNull(viewModel.aviso.value)
    }
}
