package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.mensajeDeError
import com.example.aplicacioncorpaliv.errorHttp
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Test

// Prueba que cada error técnico se convierte en la frase correcta para mostrarle a la persona.
class ErroresApiTest {

    @Test
    fun `400 usa el mensaje que envia el servidor`() {
        val e = errorHttp(400, """{"message":"El monto mínimo es 1000"}""")
        assertEquals("El monto mínimo es 1000", mensajeDeError(e))
    }

    @Test
    fun `400 sin mensaje usa el texto por defecto`() {
        assertEquals("Revisa los datos ingresados.", mensajeDeError(errorHttp(400)))
    }

    @Test
    fun `400 con cuerpo que no es JSON usa el texto por defecto`() {
        assertEquals("Revisa los datos ingresados.", mensajeDeError(errorHttp(400, "esto no es json")))
    }

    @Test
    fun `401 avisa que la sesion expiro`() {
        assertEquals(
            "Tu sesión expiró o no tienes permiso. Inicia sesión de nuevo.",
            mensajeDeError(errorHttp(401))
        )
    }

    @Test
    fun `404 avisa que no se encontro`() {
        assertEquals("No se encontró lo que se buscaba.", mensajeDeError(errorHttp(404)))
    }

    @Test
    fun `503 usa el mensaje del servidor o uno por defecto`() {
        assertEquals("Catálogo caído", mensajeDeError(errorHttp(503, """{"message":"Catálogo caído"}""")))
        assertEquals(
            "Un servicio del servidor no está disponible por ahora.",
            mensajeDeError(errorHttp(503))
        )
    }

    @Test
    fun `otro codigo muestra el numero del error`() {
        assertEquals("El servidor respondió con un error (500).", mensajeDeError(errorHttp(500)))
    }

    @Test
    fun `sin conexion avisa que no hay conexion`() {
        assertEquals(
            "No hay conexión con el servidor. Revisa que esté encendido y que tengas internet.",
            mensajeDeError(IOException("timeout"))
        )
    }

    @Test
    fun `un error desconocido muestra un mensaje generico`() {
        assertEquals("Ocurrió un error inesperado.", mensajeDeError(IllegalStateException("raro")))
    }
}
