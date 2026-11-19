package com.example.aplicacioncorpaliv

import com.example.aplicacioncorpaliv.data.Registro
import com.example.aplicacioncorpaliv.model.EstadoSolicitud
import com.example.aplicacioncorpaliv.model.Seguimiento
import com.example.aplicacioncorpaliv.model.Solicitud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import retrofit2.HttpException
import retrofit2.Response

// AYUDAS COMPARTIDAS por todos los tests (todos los datos son inventados).

// EXPLICAR: los ViewModel usan viewModelScope, que trabaja con el hilo principal de Android (Dispatchers.Main).
// En un test no existe ese hilo, así que esta regla lo reemplaza por uno de prueba que ejecuta todo al instante.
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

// Crea el error que lanzaría Retrofit si el servidor responde con ese código (400, 401, 404...).
fun errorHttp(codigo: Int, cuerpo: String = "{}"): HttpException =
    HttpException(Response.error<Any>(codigo, cuerpo.toResponseBody("application/json".toMediaType())))

fun registroDePrueba(
    id: Int = 1,
    codigo: String = "COL-AAAA2222",
    estado: String = EstadoSolicitud.RECIBIDA,
    observacion: String = ""
) = Registro(
    id = id,
    nombre = "Ana Prueba",
    mensaje = "Quiero colaborar",
    monto = 5000,
    tipo = "DONACION",
    fechaContacto = null,
    aceptaContacto = true,
    prioritario = false,
    estado = estado,
    observacion = observacion,
    medioContacto = "ana@ejemplo.cl",
    codigoSeguimiento = codigo
)

fun solicitudDePrueba(id: Int = 1, estado: String = EstadoSolicitud.RECIBIDA) = Solicitud(
    id = id,
    codigoSeguimiento = "COL-AAAA2222",
    nombre = "Ana Prueba",
    medioContacto = "ana@ejemplo.cl",
    tipo = "DONACION",
    estado = estado
)

fun seguimientoDePrueba(estado: String = EstadoSolicitud.RECIBIDA, observacion: String? = null) = Seguimiento(
    codigoSeguimiento = "COL-AAAA2222",
    tipo = "DONACION",
    estado = estado,
    observacion = observacion
)
