package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.ContactoRepository
import com.example.aplicacioncorpaliv.data.PerfilDataStore
import com.example.aplicacioncorpaliv.data.remote.mensajeDeError
import com.example.aplicacioncorpaliv.model.ContactoModel
import com.example.aplicacioncorpaliv.model.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ContactoErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val mensaje: String? = null,
    val privacidad: String? = null
)

class ContactoViewModel(application: Application) : AndroidViewModel(application) {
    private val repositorio = ContactoRepository(AppDatabase.getInstance(application).contactoDao())
    private val perfil = PerfilDataStore(application)

    private val _formulario = MutableStateFlow(ContactoModel())
    val formulario: StateFlow<ContactoModel> = _formulario.asStateFlow()
    private val _errores = MutableStateFlow(ContactoErrores())
    val errores: StateFlow<ContactoErrores> = _errores.asStateFlow()
    private val _guardadoCorrectamente = MutableStateFlow(false)
    val guardadoCorrectamente: StateFlow<Boolean> = _guardadoCorrectamente.asStateFlow()

    // Igual que en el formulario de colaboración: 'enviando' mientras se espera al servidor y 'errorEnvio' si falla
    private val _enviando = MutableStateFlow(false)
    val enviando: StateFlow<Boolean> = _enviando.asStateFlow()
    private val _errorEnvio = MutableStateFlow<String?>(null)
    val errorEnvio: StateFlow<String?> = _errorEnvio.asStateFlow()

    fun actualizarNombre(valor: String) {
        _formulario.value = _formulario.value.copy(nombre = valor)
    }
    fun actualizarCorreo(valor: String) {
        _formulario.value = _formulario.value.copy(correo = valor)
    }
    fun actualizarMensaje(valor: String) {
        _formulario.value = _formulario.value.copy(mensaje = valor)
    }
    fun actualizarAceptaPrivacidad(valor: Boolean) {
        _formulario.value = _formulario.value.copy(aceptaPrivacidad = valor)
    }

    // EXPLICAR: trae el nombre y el correo guardados en 'Perfil' y los escribe en los campos que estén vacíos.
    // En Perfil el contacto puede ser un teléfono; como aquí se pide un correo, solo se usa si contiene una @.
    fun precargarDesdePerfil() {
        viewModelScope.launch {
            val nombrePerfil = perfil.nombre.first()
            val contactoPerfil = perfil.medioContacto.first()
            val correoPerfil = if (contactoPerfil.contains("@")) contactoPerfil else ""
            val actual = _formulario.value
            _formulario.value = actual.copy(
                nombre = actual.nombre.ifBlank { nombrePerfil },
                correo = actual.correo.ifBlank { correoPerfil }
            )
        }
    }

    // Reglas del formulario | Las reglas están en model/Validaciones.kt (nombre, correo y mensaje). Además se exige aceptar la privacidad
    fun validarFormulario(): Boolean {
        val datos = _formulario.value

        val nuevosErrores = ContactoErrores(
            nombre = Validaciones.nombre(datos.nombre),
            correo = Validaciones.correo(datos.correo),
            mensaje = Validaciones.mensaje(datos.mensaje),
            privacidad = if (datos.aceptaPrivacidad) null else "Debes aceptar la política de privacidad para enviar"
        )
        _errores.value = nuevosErrores
        return nuevosErrores == ContactoErrores()
    }
    fun guardar(): Boolean {
        if (!validarFormulario()) {
            return false
        }
        viewModelScope.launch {
            _enviando.value = true
            _errorEnvio.value = null
            try {
                repositorio.save(_formulario.value)
                _formulario.value = ContactoModel()
                _guardadoCorrectamente.value = true
            } catch (e: Exception) {
                _errorEnvio.value = mensajeDeError(e)
            }
            _enviando.value = false
        }
        return true
    }
    fun cerrarErrorEnvio() {
        _errorEnvio.value = null
    }
    fun ocultarMensajeGuardado() {
        _guardadoCorrectamente.value = false
    }
}
