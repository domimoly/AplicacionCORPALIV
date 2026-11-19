package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.VinculacionApi
import com.example.aplicacioncorpaliv.model.ContactoModel
import com.example.aplicacioncorpaliv.model.Seguimiento
import com.example.aplicacioncorpaliv.model.SolicitudRequest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import java.io.IOException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContactoRepositoryTest {

    private val dao = mockk<ContactoDao>()
    private val api = mockk<VinculacionApi>()

    private fun crearRepositorio(): ContactoRepository {
        every { dao.observeAll() } returns flowOf(emptyList())
        return ContactoRepository(dao, api)
    }

    private val datos = ContactoModel(
        nombre = " Ana Prueba ",
        correo = " ana@ejemplo.cl ",
        mensaje = " Hola, tengo una consulta ",
        aceptaPrivacidad = true
    )

    @Test
    fun `save envia el mensaje como solicitud de tipo OTRO y guarda una copia`() = runTest {
        val enviada = slot<SolicitudRequest>()
        val guardado = slot<Contacto>()
        coEvery { api.crear(capture(enviada)) } returns Seguimiento("COL-AAAA2222", "OTRO", "RECIBIDA")
        coEvery { dao.insert(capture(guardado)) } just Runs
        val repositorio = crearRepositorio()

        repositorio.save(datos)

        assertEquals("OTRO", enviada.captured.tipo)
        assertEquals("Ana Prueba", enviada.captured.nombre)
        assertEquals("ana@ejemplo.cl", enviada.captured.medioContacto)
        assertEquals("Hola, tengo una consulta", enviada.captured.mensaje)
        assertTrue(enviada.captured.aceptaPrivacidad)
        assertEquals("ana@ejemplo.cl", guardado.captured.correo)
    }

    @Test
    fun `save no guarda la copia si el servidor falla`() = runTest {
        coEvery { api.crear(any()) } throws IOException("sin conexión")
        val repositorio = crearRepositorio()

        val resultado = runCatching { repositorio.save(datos) }

        assertTrue(resultado.exceptionOrNull() is IOException)
        coVerify(exactly = 0) { dao.insert(any()) }
    }

    @Test
    fun `delete elimina el mensaje de la base local`() = runTest {
        val contacto = Contacto(id = 1, nombre = "Ana Prueba", correo = "ana@ejemplo.cl", mensaje = "Hola")
        coEvery { dao.delete(contacto) } just Runs
        val repositorio = crearRepositorio()

        repositorio.delete(contacto)

        coVerify { dao.delete(contacto) }
    }

    @Test
    fun `contactos entrega la lista guardada en el telefono`() = runTest {
        val contacto = Contacto(id = 1, nombre = "Ana Prueba", correo = "ana@ejemplo.cl", mensaje = "Hola")
        every { dao.observeAll() } returns flowOf(listOf(contacto))
        val repositorio = ContactoRepository(dao, api)

        assertEquals(listOf(contacto), repositorio.contactos.first())
    }
}
