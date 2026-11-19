package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.RetrofitInstance
import com.example.aplicacioncorpaliv.data.remote.VinculacionApi
import com.example.aplicacioncorpaliv.model.ContactoModel
import com.example.aplicacioncorpaliv.model.SolicitudRequest
import com.example.aplicacioncorpaliv.model.TiposColaboracion
import kotlinx.coroutines.flow.Flow

// El repositorio de contactos sigue la misma idea que RegistroRepository.
// EXPLICAR: un mensaje de Contacto se envía al servidor como una solicitud de tipo OTRO (así llega al panel del gestor)
// y, si el servidor la acepta, se guarda una copia en el teléfono.
class ContactoRepository(
    private val dao: ContactoDao,
    private val api: VinculacionApi = RetrofitInstance.vinculacion
) {
    val contactos: Flow<List<Contacto>> = dao.observeAll()

    suspend fun save(datos: ContactoModel) {
        api.crear(
            SolicitudRequest(
                nombre = datos.nombre.trim(),
                medioContacto = datos.correo.trim(),
                tipo = TiposColaboracion.OTRO,
                mensaje = datos.mensaje.trim(),
                aceptaPrivacidad = datos.aceptaPrivacidad
            )
        )
        dao.insert(
            Contacto(
                nombre = datos.nombre.trim(),
                correo = datos.correo.trim(),
                mensaje = datos.mensaje.trim()
            )
        )
    }

    suspend fun delete(contacto: Contacto) {
        dao.delete(contacto)
    }
}
