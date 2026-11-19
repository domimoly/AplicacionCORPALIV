package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.PerfilDataStore
import com.example.aplicacioncorpaliv.model.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class PerfilErrores(
    val nombre: String? = null,
    val medioContacto: String? = null
)

// PERFIL: la imagen (cámara o galería) y los datos de la persona (nombre y correo o teléfono).
// EXPLICAR: antes esta clase era un ViewModel simple. Ahora es AndroidViewModel porque necesita el contexto de la app
// para usar DataStore (donde se guardan el nombre y el contacto en el teléfono).
// AVANZADO: @JvmOverloads hace que Kotlin cree también el constructor que recibe SOLO 'application' (el que usa viewModel() en la app).
// Los demás parámetros tienen un valor por defecto, y en los tests unitarios se reemplazan por objetos falsos (MockK).
class PerfilViewModel @JvmOverloads constructor(
    application: Application,
    private val dataStore: PerfilDataStore = PerfilDataStore(application)
) : AndroidViewModel(application) {

    private val _imagenUri = MutableStateFlow<Uri?>(null)
    val imagenUri: StateFlow<Uri?> = _imagenUri.asStateFlow()

    private val _nombre = MutableStateFlow("")
    val nombre: StateFlow<String> = _nombre.asStateFlow()

    private val _medioContacto = MutableStateFlow("")
    val medioContacto: StateFlow<String> = _medioContacto.asStateFlow()

    private val _errores = MutableStateFlow(PerfilErrores())
    val errores: StateFlow<PerfilErrores> = _errores.asStateFlow()

    private val _guardadoCorrectamente = MutableStateFlow(false)
    val guardadoCorrectamente: StateFlow<Boolean> = _guardadoCorrectamente.asStateFlow()

    // init se ejecuta una vez al crear el ViewModel: trae lo que ya estaba guardado en el teléfono
    init {
        viewModelScope.launch {
            _nombre.value = dataStore.nombre.first()
            _medioContacto.value = dataStore.medioContacto.first()
        }
    }

    fun actualizarImagenDesdeGaleria(uri: Uri?) {
        if (uri != null) _imagenUri.value = uri
    }

    fun actualizarImagenDesdeCamara(uri: Uri?, capturaCorrecta: Boolean) {
        if (capturaCorrecta && uri != null) {
            _imagenUri.value = uri
        }
    }

    fun actualizarNombre(valor: String) {
        _nombre.value = valor
    }

    fun actualizarMedioContacto(valor: String) {
        _medioContacto.value = valor
    }

    // Los dos datos son opcionales: se pueden dejar vacíos. Si se escribe algo, se valida con las reglas de Validaciones.kt
    fun guardarDatos() {
        val nuevosErrores = PerfilErrores(
            nombre = Validaciones.nombreOpcional(_nombre.value),
            medioContacto = Validaciones.medioContactoOpcional(_medioContacto.value)
        )
        _errores.value = nuevosErrores
        if (nuevosErrores != PerfilErrores()) {
            return
        }
        viewModelScope.launch {
            dataStore.guardar(_nombre.value.trim(), _medioContacto.value.trim())
            _guardadoCorrectamente.value = true
        }
    }

    fun ocultarMensajeGuardado() {
        _guardadoCorrectamente.value = false
    }
}
