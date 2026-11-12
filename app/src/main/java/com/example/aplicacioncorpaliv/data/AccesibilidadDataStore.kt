package com.example.aplicacioncorpaliv.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Archivo de preferencias propio de accesibilidad (distinto al de Novedades)
private val Context.dataStoreAccesibilidad by preferencesDataStore(
    name = "preferencias_accesibilidad"
)

// EXPLICAR: igual que EstadoDataStore (Novedades), pero guarda DOS preferencias en el teléfono:
// - nivel de texto: un número (0 = normal, 1 = grande, 2 = muy grande)
// - alto contraste: verdadero o falso
// Quedan guardadas aunque se cierre la app.
class AccesibilidadDataStore(private val context: Context) {

    companion object {
        private val NIVEL_TEXTO = intPreferencesKey("nivel_texto")
        private val ALTO_CONTRASTE = booleanPreferencesKey("alto_contraste")
    }

    // Flow = flujo de datos: cada vez que el valor guardado cambia, avisa a quien lo esté mirando
    val nivelTexto: Flow<Int> =
        context.dataStoreAccesibilidad.data.map { preferencias ->
            preferencias[NIVEL_TEXTO] ?: 0
        }

    val altoContraste: Flow<Boolean> =
        context.dataStoreAccesibilidad.data.map { preferencias ->
            preferencias[ALTO_CONTRASTE] ?: false
        }

    suspend fun guardarNivelTexto(nivel: Int) {
        context.dataStoreAccesibilidad.edit { preferencias ->
            preferencias[NIVEL_TEXTO] = nivel
        }
    }

    suspend fun guardarAltoContraste(activado: Boolean) {
        context.dataStoreAccesibilidad.edit { preferencias ->
            preferencias[ALTO_CONTRASTE] = activado
        }
    }
}
