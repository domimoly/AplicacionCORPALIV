package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.model.FormularioModel
import kotlinx.coroutines.flow.Flow

// Este funciona como intermediario entre los ViewModels y el DAO. Conviertiendo el formulario en un Registro y lo guarda
class RegistroRepository(private val dao: RegistroDao) {
    val registros: Flow<List<Registro>> = dao.observeAll()

    suspend fun save(datos: FormularioModel) {
        dao.insert(
            Registro(
                nombre = datos.nombre.trim(),
                mensaje = datos.descripcion.trim(),
                monto = datos.cantidad.toInt(),
                tipo = datos.categoria,
                fechaContacto = datos.fecha,
                aceptaContacto = datos.aceptaTerminos,
                prioritario = datos.activo
            )
        )
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
