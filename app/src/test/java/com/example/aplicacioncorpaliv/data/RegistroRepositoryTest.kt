package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.VinculacionApi
import com.example.aplicacioncorpaliv.errorHttp
import com.example.aplicacioncorpaliv.model.FormularioModel
import com.example.aplicacioncorpaliv.model.Seguimiento
import com.example.aplicacioncorpaliv.model.SolicitudRequest
import com.example.aplicacioncorpaliv.registroDePrueba
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import java.io.IOException
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

// EXPLICAR: estos tests NO usan internet ni base de datos real. El DAO y la API son 'mocks' (copias falsas hechas con MockK):
// con coEvery se les dice qué responder, y con coVerify se comprueba que el repositorio las llamó como correspondía.
class RegistroRepositoryTest {

    private val dao = mockk<RegistroDao>()
    private val api = mockk<VinculacionApi>()

    private fun crearRepositorio(registros: List<Registro> = emptyList()): RegistroRepository {
        every { dao.observeAll() } returns flowOf(registros)
        return RegistroRepository(dao, api)
    }

    private fun formularioDonacion() = FormularioModel(
        nombre = "  Ana Prueba ",
        medioContacto = " ana@ejemplo.cl ",
        descripcion = " Quiero donar ",
        cantidad = "5000",
        aceptaTerminos = true,
        activo = true,
        fecha = 1_700_000_000_000L, // 14 de noviembre de 2023 (UTC)
        categoria = "DONACION"
    )

    @Test
    fun `save envia la solicitud al servidor y guarda una copia con el codigo del servidor`() = runTest {
        val enviada = slot<SolicitudRequest>()
        val guardada = slot<Registro>()
        coEvery { api.crear(capture(enviada)) } returns
            Seguimiento("COL-AAAA2222", "DONACION", "RECIBIDA")
        coEvery { dao.insert(capture(guardada)) } just Runs
        val repositorio = crearRepositorio()

        val codigo = repositorio.save(formularioDonacion())

        assertEquals("COL-AAAA2222", codigo)
        // Lo enviado: textos sin espacios sobrantes, monto, fecha como 'aaaa-mm-dd' y privacidad aceptada
        assertEquals("Ana Prueba", enviada.captured.nombre)
        assertEquals("ana@ejemplo.cl", enviada.captured.medioContacto)
        assertEquals("Quiero donar", enviada.captured.mensaje)
        assertEquals(5000, enviada.captured.monto)
        assertEquals("2023-11-14", enviada.captured.fechaContacto)
        assertTrue(enviada.captured.prioritario)
        assertTrue(enviada.captured.aceptaPrivacidad)
        // Lo guardado en el teléfono
        assertEquals("COL-AAAA2222", guardada.captured.codigoSeguimiento)
        assertEquals("RECIBIDA", guardada.captured.estado)
        assertEquals(5000, guardada.captured.monto)
    }

    @Test
    fun `save no envia monto cuando el tipo no es donacion`() = runTest {
        val enviada = slot<SolicitudRequest>()
        coEvery { api.crear(capture(enviada)) } returns Seguimiento("COL-BBBB3333", "VOLUNTARIADO", "RECIBIDA")
        coEvery { dao.insert(any()) } just Runs
        val repositorio = crearRepositorio()

        repositorio.save(formularioDonacion().copy(categoria = "VOLUNTARIADO", fecha = null))

        assertNull(enviada.captured.monto)
        assertNull(enviada.captured.fechaContacto)
    }

    @Test
    fun `save no guarda nada si el servidor falla`() = runTest {
        coEvery { api.crear(any()) } throws IOException("sin conexión")
        val repositorio = crearRepositorio()

        val resultado = runCatching { repositorio.save(formularioDonacion()) }

        assertTrue(resultado.exceptionOrNull() is IOException)
        coVerify(exactly = 0) { dao.insert(any()) }
    }

    @Test
    fun `consultarSeguimiento normaliza el codigo antes de preguntar al servidor`() = runTest {
        coEvery { api.seguimiento("COL-AAAA2222") } returns Seguimiento("COL-AAAA2222", "DONACION", "EN_REVISION")
        val repositorio = crearRepositorio()

        val respuesta = repositorio.consultarSeguimiento("  col-aaaa2222 ")

        assertEquals("EN_REVISION", respuesta.estado)
    }

    @Test
    fun `actualizarEstados cambia la copia local cuando el estado cambio en el servidor`() = runTest {
        val registro = registroDePrueba()
        coEvery { api.seguimiento("COL-AAAA2222") } returns
            Seguimiento("COL-AAAA2222", "DONACION", "EN_REVISION", "Te llamaremos")
        coEvery { dao.update(any()) } just Runs
        val repositorio = crearRepositorio(listOf(registro))

        repositorio.actualizarEstados()

        coVerify { dao.update(registro.copy(estado = "EN_REVISION", observacion = "Te llamaremos")) }
    }

    @Test
    fun `actualizarEstados no toca la base si no hubo cambios`() = runTest {
        coEvery { api.seguimiento("COL-AAAA2222") } returns Seguimiento("COL-AAAA2222", "DONACION", "RECIBIDA")
        val repositorio = crearRepositorio(listOf(registroDePrueba()))

        repositorio.actualizarEstados()

        coVerify(exactly = 0) { dao.update(any()) }
    }

    @Test
    fun `actualizarEstados salta las solicitudes sin codigo y las que el servidor ya no tiene`() = runTest {
        val sinCodigo = registroDePrueba(id = 1, codigo = "")
        val borrada = registroDePrueba(id = 2, codigo = "COL-CCCC4444")
        val vigente = registroDePrueba(id = 3, codigo = "COL-DDDD5555")
        coEvery { api.seguimiento("COL-CCCC4444") } throws errorHttp(404)
        coEvery { api.seguimiento("COL-DDDD5555") } returns Seguimiento("COL-DDDD5555", "DONACION", "CONTACTADO")
        coEvery { dao.update(any()) } just Runs
        val repositorio = crearRepositorio(listOf(sinCodigo, borrada, vigente))

        repositorio.actualizarEstados()

        coVerify(exactly = 0) { api.seguimiento("") }
        coVerify(exactly = 1) { dao.update(vigente.copy(estado = "CONTACTADO")) }
    }

    @Test
    fun `actualizarEstados avisa hacia arriba cuando no hay conexion`() = runTest {
        coEvery { api.seguimiento(any()) } throws IOException("sin conexión")
        val repositorio = crearRepositorio(listOf(registroDePrueba()))

        val resultado = runCatching { repositorio.actualizarEstados() }

        assertTrue(resultado.exceptionOrNull() is IOException)
    }

    @Test
    fun `delete quita la solicitud del telefono sin llamar al servidor`() = runTest {
        val registro = registroDePrueba()
        coEvery { dao.delete(registro) } just Runs
        val repositorio = crearRepositorio()

        repositorio.delete(registro)

        coVerify { dao.delete(registro) }
    }
}
