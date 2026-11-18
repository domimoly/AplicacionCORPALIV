package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.model.CodigoSeguimiento
import com.example.aplicacioncorpaliv.model.FormularioModel
import com.example.aplicacioncorpaliv.model.TiposColaboracion
import kotlinx.coroutines.flow.Flow

// Este funciona como intermediario entre los ViewModels y el DAO. Conviertiendo el formulario en un Registro y lo guarda
class RegistroRepository(private val dao: RegistroDao) {
    val registros: Flow<List<Registro>> = dao.observeAll()

    // EXPLICAR: guarda la solicitud y DEVUELVE su código de seguimiento, para mostrárselo a la persona.
    // El monto solo cuenta en las donaciones; en los demás tipos se guarda 0.
    suspend fun save(datos: FormularioModel): String {
        val codigo = CodigoSeguimiento.generar()
        dao.insert(
            Registro(
                nombre = datos.nombre.trim(),
                mensaje = datos.descripcion.trim(),
                monto = if (datos.categoria == TiposColaboracion.DONACION) datos.cantidad.toIntOrNull() ?: 0 else 0,
                tipo = datos.categoria,
                fechaContacto = datos.fecha,
                aceptaContacto = datos.aceptaTerminos,
                prioritario = datos.activo,
                medioContacto = datos.medioContacto.trim(),
                codigoSeguimiento = codigo
            )
        )
        return codigo
    }

    // EXPLICAR: consulta una solicitud con su código. Devuelve null si no existe.
    // Cuando se conecte el backend, esta función llamará a GET /solicitudes/seguimiento/{codigo} de ms-vinculacion.
    suspend fun buscarPorCodigo(codigo: String): Registro? {
        return dao.findByCodigo(CodigoSeguimiento.normalizar(codigo))
    }

    // EXPLICAR: copy() crea una copia de la solicitud cambiando solo estas dos cosas; el resto queda igual.
    // Luego el DAO actualiza esa fila en la base de datos.
    suspend fun cambiarEstado(registro: Registro, estado: String, observacion: String) {
        dao.update(registro.copy(estado = estado, observacion = observacion.trim()))
    }

    suspend fun delete(registro: Registro) {
        dao.delete(registro)
    }
}
