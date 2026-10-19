package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.model.ContactoModel
import kotlinx.coroutines.flow.Flow

class ContactoRepository(private val dao: ContactoDao) {
    val contactos: Flow<List<Contacto>> = dao.observeAll()

    suspend fun save(datos: ContactoModel) {
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