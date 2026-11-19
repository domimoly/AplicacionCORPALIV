package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.FeriadosApi
import com.example.aplicacioncorpaliv.model.Feriado
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FeriadosRepositoryTest {

    private val api = mockk<FeriadosApi>()
    private val repositorio = FeriadosRepository(api)

    private val feriadosDelAnio = listOf(
        Feriado("2026-12-25", "Navidad", "Christmas Day"),
        Feriado("2026-01-01", "Año Nuevo", "New Year's Day"),
        Feriado("2026-12-08", "Inmaculada Concepción", "Immaculate Conception"),
        Feriado("2026-11-01", "Día de Todos los Santos", "All Saints' Day"),
        Feriado("2026-11-19", "Feriado de prueba", "Test holiday")
    )

    @Test
    fun `proximos deja solo los feriados desde hoy y en orden de fecha`() = runTest {
        coEvery { api.feriados(2026, "CL") } returns feriadosDelAnio

        val proximos = repositorio.proximos("2026-11-19")

        // El del 19 de noviembre cuenta (es hoy); los anteriores no
        assertEquals(listOf("2026-11-19", "2026-12-08", "2026-12-25"), proximos.map { it.date })
    }

    @Test
    fun `proximos respeta la cantidad pedida`() = runTest {
        coEvery { api.feriados(2026, "CL") } returns feriadosDelAnio

        assertEquals(1, repositorio.proximos("2026-01-01", cantidad = 1).size)
    }

    @Test
    fun `proximos devuelve lista vacia si ya no quedan feriados`() = runTest {
        coEvery { api.feriados(2026, "CL") } returns feriadosDelAnio

        assertTrue(repositorio.proximos("2026-12-31").isEmpty())
    }
}
