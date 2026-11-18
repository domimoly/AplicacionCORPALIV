package com.example.aplicacioncorpaliv.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStorePerfil by preferencesDataStore(
    name = "preferencias_perfil"
)

// DATOS DEL PERFIL guardados en el teléfono con DataStore (el mismo recurso que EstadoDataStore, pero con textos).
// EXPLICAR: la persona escribe su nombre y su correo o teléfono UNA vez en 'Perfil', y los formularios Colaborar y Contacto
// los traen escritos. Los datos quedan solo en este teléfono: no se envían a ninguna parte hasta que la persona envía un formulario.
class PerfilDataStore(private val context: Context) {

    companion object {
        private val NOMBRE = stringPreferencesKey("perfil_nombre")
        private val MEDIO_CONTACTO = stringPreferencesKey("perfil_medio_contacto")
    }

    // ?: "" significa 'si todavía no hay nada guardado, usa texto vacío'
    val nombre: Flow<String> =
        context.dataStorePerfil.data.map { preferencias -> preferencias[NOMBRE] ?: "" }

    val medioContacto: Flow<String> =
        context.dataStorePerfil.data.map { preferencias -> preferencias[MEDIO_CONTACTO] ?: "" }

    suspend fun guardar(nombre: String, medioContacto: String) {
        context.dataStorePerfil.edit { preferencias ->
            preferencias[NOMBRE] = nombre
            preferencias[MEDIO_CONTACTO] = medioContacto
        }
    }
}
