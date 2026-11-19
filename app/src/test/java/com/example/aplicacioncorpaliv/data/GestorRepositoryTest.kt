package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.AdminApi
import com.example.aplicacioncorpaliv.data.remote.VinculacionApi
import com.example.aplicacioncorpaliv.model.CambioEstado
import com.example.aplicacioncorpaliv.model.LoginRequest
import com.example.aplicacioncorpaliv.model.LoginResponse
import com.example.aplicacioncorpaliv.solicitudDePrueba
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GestorRepositoryTest {

    private val adminApi = mockk<AdminApi>()
    private val vinculacionApi = mockk<VinculacionApi>()
    private val repositorio = GestorRepository(adminApi, vinculacionApi)

    @Test
    fun `iniciarSesion envia el correo en minusculas y sin espacios`() = runTest {
        val respuesta = LoginResponse("token-falso", "Gestor Prueba", "gestor@ejemplo.cl", "GESTOR")
        coEvery { adminApi.login(LoginRequest("gestor@ejemplo.cl", "Clave123")) } returns respuesta

        val resultado = repositorio.iniciarSesion("  Gestor@Ejemplo.CL ", "Clave123")

        assertEquals("token-falso", resultado.token)
    }

    @Test
    fun `listar envia el token con el formato Bearer`() = runTest {
        coEvery { vinculacionApi.listar("Bearer token-falso") } returns listOf(solicitudDePrueba())

        val lista = repositorio.listar("token-falso")

        assertEquals(1, lista.size)
    }

    @Test
    fun `cambiarEstado envia el token, el id y la observacion sin espacios sobrantes`() = runTest {
        val actualizada = solicitudDePrueba(id = 7, estado = "EN_REVISION")
        coEvery {
            vinculacionApi.cambiarEstado("Bearer token-falso", 7L, CambioEstado("EN_REVISION", "Te llamamos"))
        } returns actualizada

        val resultado = repositorio.cambiarEstado("token-falso", 7, "EN_REVISION", "  Te llamamos ")

        assertEquals("EN_REVISION", resultado.estado)
    }

    @Test
    fun `eliminar llama al servidor con el id de la solicitud`() = runTest {
        coEvery { vinculacionApi.eliminar("Bearer token-falso", 7L) } just Runs

        repositorio.eliminar("token-falso", 7)

        coVerify { vinculacionApi.eliminar("Bearer token-falso", 7L) }
    }
}
