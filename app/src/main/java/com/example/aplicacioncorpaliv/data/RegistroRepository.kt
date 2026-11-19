package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.RetrofitInstance
import com.example.aplicacioncorpaliv.data.remote.VinculacionApi
import com.example.aplicacioncorpaliv.model.CodigoSeguimiento
import com.example.aplicacioncorpaliv.model.FormularioModel
import com.example.aplicacioncorpaliv.model.Seguimiento
import com.example.aplicacioncorpaliv.model.SolicitudRequest
import com.example.aplicacioncorpaliv.model.TiposColaboracion
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

// REPOSITORIO DE SOLICITUDES: intermediario entre los ViewModel, el servidor (ms-vinculacion) y la base de datos del teléfono.
// EXPLICAR: la solicitud se ENVÍA al servidor, que es quien crea el código de seguimiento. Si el servidor la acepta,
// se guarda además una COPIA en Room, para que 'Mis solicitudes' la muestre aunque no haya internet.
class RegistroRepository(
    private val dao: RegistroDao,
    private val api: VinculacionApi = RetrofitInstance.vinculacion
) {
    val registros: Flow<List<Registro>> = dao.observeAll()

    // Envía la solicitud y DEVUELVE su código de seguimiento. Si el servidor falla, lanza un error y NO se guarda nada.
    suspend fun save(datos: FormularioModel): String {
        // El monto solo cuenta en las donaciones; en los demás tipos no se envía
        val esDonacion = datos.categoria == TiposColaboracion.DONACION
        val solicitud = SolicitudRequest(
            nombre = datos.nombre.trim(),
            medioContacto = datos.medioContacto.trim(),
            tipo = datos.categoria,
            mensaje = datos.descripcion.trim(),
            monto = if (esDonacion) datos.cantidad.toIntOrNull() else null,
            fechaContacto = datos.fecha?.let { fechaATexto(it) },
            prioritario = datos.activo,
            aceptaPrivacidad = datos.aceptaTerminos
        )
        val respuesta = api.crear(solicitud)

        dao.insert(
            Registro(
                nombre = solicitud.nombre,
                mensaje = solicitud.mensaje.orEmpty(),
                monto = solicitud.monto ?: 0,
                tipo = solicitud.tipo,
                fechaContacto = datos.fecha,
                aceptaContacto = datos.aceptaTerminos,
                prioritario = datos.activo,
                estado = respuesta.estado,
                medioContacto = solicitud.medioContacto,
                codigoSeguimiento = respuesta.codigoSeguimiento
            )
        )
        return respuesta.codigoSeguimiento
    }

    // Consulta el estado de una solicitud en el servidor con su código (GET /solicitudes/seguimiento/{codigo}).
    // Si no existe, el servidor responde 404 y se lanza un HttpException.
    suspend fun consultarSeguimiento(codigo: String): Seguimiento {
        return api.seguimiento(CodigoSeguimiento.normalizar(codigo))
    }

    // EXPLICAR: recorre las solicitudes guardadas en el teléfono, le pregunta al servidor el estado actual de cada una
    // y actualiza la copia local si cambió (por ejemplo, cuando el gestor la pasó a 'En revisión').
    suspend fun actualizarEstados() {
        for (registro in registros.first()) {
            if (registro.codigoSeguimiento.isBlank()) continue
            try {
                val actual = api.seguimiento(registro.codigoSeguimiento)
                val observacion = actual.observacion.orEmpty()
                if (actual.estado != registro.estado || observacion != registro.observacion) {
                    dao.update(registro.copy(estado = actual.estado, observacion = observacion))
                }
            } catch (e: HttpException) {
                // Si el servidor ya no tiene esa solicitud (404), se deja como está y se sigue con la siguiente
                continue
            } catch (e: IOException) {
                // Sin conexión: se avisa hacia arriba para mostrar un mensaje
                throw e
            }
        }
    }

    // Quita la solicitud de este teléfono (no la borra del servidor)
    suspend fun delete(registro: Registro) {
        dao.delete(registro)
    }

    // DatePicker entrega la fecha en milisegundos (UTC). El servidor espera texto 'aaaa-mm-dd'
    private fun fechaATexto(milisegundos: Long): String {
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        formato.timeZone = TimeZone.getTimeZone("UTC")
        return formato.format(Date(milisegundos))
    }
}
