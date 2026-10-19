package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.ContactoRepository
import com.example.aplicacioncorpaliv.model.ContactoModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ContactoErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val mensaje: String? = null
)

class ContactoViewModel(application: Application) : AndroidViewModel(application) {
    private val repositorio = ContactoRepository(AppDatabase.getInstance(application).contactoDao())

    private val _formulario = MutableStateFlow(ContactoModel())
    val formulario: StateFlow<ContactoModel> = _formulario.asStateFlow()
    private val _errores = MutableStateFlow(ContactoErrores())
    val errores: StateFlow<ContactoErrores> = _errores.asStateFlow()
    private val _guardadoCorrectamente = MutableStateFlow(false)
    val guardadoCorrectamente: StateFlow<Boolean> = _guardadoCorrectamente.asStateFlow()

    fun actualizarNombre(valor: String) {
        _formulario.value = _formulario.value.copy(nombre = valor)
    }
    fun actualizarCorreo(valor: String) {
        _formulario.value = _formulario.value.copy(correo = valor)
    }
    fun actualizarMensaje(valor: String) {
        _formulario.value = _formulario.value.copy(mensaje = valor)
    }
    fun validarFormulario(): Boolean {
        val datos = _formulario.value
        var nombreError: String? = null
        var correoError: String? = null
        var mensajeError: String? = null

        if (datos.nombre.isBlank()) {
            nombreError = "El nombre es obligatorio"
        } else if (datos.nombre.length < 3) {
            nombreError = "El nombre debe tener al menos 3 caracteres"
        }
        if (datos.correo.isBlank()) {
            correoError = "El correo es obligatorio"
        } else if (!datos.correo.contains("@") || !datos.correo.contains(".")) {
            correoError = "El correo no tiene un formato válido"
        }
        if (datos.mensaje.isBlank()) {
            mensajeError = "El mensaje es obligatorio"
        } else if (datos.mensaje.length < 5) {
            mensajeError = "El mensaje debe tener al menos 5 caracteres"
        }

        val nuevosErrores =
            ContactoErrores(
                nombre = nombreError,
                correo = correoError,
                mensaje = mensajeError
            )
        _errores.value = nuevosErrores
        return nuevosErrores == ContactoErrores()
    }
    fun guardar(): Boolean {
        if (!validarFormulario()) {
            return false
        }
        viewModelScope.launch {
            repositorio.save(_formulario.value)
            _formulario.value = ContactoModel()
            _guardadoCorrectamente.value = true
        }
        return true
    }
    fun ocultarMensajeGuardado() {
        _guardadoCorrectamente.value = false
    }
}