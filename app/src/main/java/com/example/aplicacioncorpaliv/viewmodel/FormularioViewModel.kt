package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.RegistroRepository
import com.example.aplicacioncorpaliv.model.FormularioModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FormularioErrores(
    val nombre: String? = null,
    val descripcion: String? = null,
    val cantidad: String? = null,
    val aceptaTerminos: String? = null,
    val fecha: String? = null,
    val categoria: String? = null
)

class FormularioViewModel(application: Application) : AndroidViewModel(application) {
    private val repositorio = RegistroRepository(AppDatabase.getInstance(application).registroDao())

    private val _formulario = MutableStateFlow(FormularioModel())
    val formulario: StateFlow<FormularioModel> = _formulario.asStateFlow()
    private val _errores = MutableStateFlow(FormularioErrores())
    val errores: StateFlow<FormularioErrores> = _errores.asStateFlow()
    private val _guardadoCorrectamente = MutableStateFlow(false)
    val guardadoCorrectamente: StateFlow<Boolean> = _guardadoCorrectamente.asStateFlow()

    fun actualizarNombre(valor: String) {
        _formulario.value = _formulario.value.copy(nombre = valor)
    }
    fun actualizarDescripcion(valor: String) {
        _formulario.value =_formulario.value.copy(descripcion = valor)
    }
    fun actualizarCantidad(valor: String) {
        if (valor.all { it.isDigit() }) {
            _formulario.value =_formulario.value.copy(cantidad = valor)
        }
    }
    fun actualizarAceptaTerminos(valor: Boolean) {
        _formulario.value = _formulario.value.copy(aceptaTerminos = valor)
    }
    fun actualizarActivo(valor: Boolean) {
        _formulario.value = _formulario.value.copy(activo = valor)
    }
    fun actualizarFecha(valor: Long?) {
        _formulario.value = _formulario.value.copy(fecha = valor)
    }
    fun actualizarCategoria(valor: String) {
        _formulario.value = _formulario.value.copy(categoria = valor)
    }
    // Reglas de validacion | Para cambiar una regla se edita este bloque
    fun validarFormulario(): Boolean {
        val datos = _formulario.value
        var nombreError: String? = null
        var descripcionError: String? = null
        var cantidadError: String? = null
        var terminosError: String? = null
        var fechaError: String? = null
        var categoriaError: String? = null

        if (datos.nombre.isBlank()) {
            nombreError = "El nombre es obligatorio"
        } else if (datos.nombre.length < 3) {
            nombreError = "El nombre debe tener al menos 3 caracteres"
        }
        if (datos.descripcion.isBlank()) {
            descripcionError = "El mensaje es obligatorio"
        } else if (datos.descripcion.length < 5) {
            descripcionError = "El mensaje debe tener al menos 5 caracteres"
        }
        // Regla del monto | debe ser un número entre 1000 y 10000
        val cantidad = datos.cantidad.toIntOrNull()
        if (cantidad == null) {
            cantidadError = "Debe ingresar un número"
        } else if (cantidad < 1000 || cantidad > 10000) {
            cantidadError = "El monto debe estar entre 1000 y 10000"
        }
        if (!datos.aceptaTerminos) {
            terminosError ="Debe aceptar ser contactado por CORPALIV"
        }
        if (datos.fecha == null) {
            fechaError ="Debe seleccionar una fecha"
        }
        if (datos.categoria.isBlank()) {
            categoriaError ="Debe seleccionar un tipo de colaboración"
        }

        val nuevosErrores =
            FormularioErrores(
                nombre = nombreError,
                descripcion = descripcionError,
                cantidad = cantidadError,
                aceptaTerminos = terminosError,
                fecha = fechaError,
                categoria = categoriaError
            )
        _errores.value = nuevosErrores
        return nuevosErrores == FormularioErrores()
    }
    // Si todo es válido | Se guarda en Room, vacía el formulario y avisa con guardadoCorrectamente
    fun guardar(): Boolean {
        if (!validarFormulario()) {
            return false
        }
        viewModelScope.launch {
            repositorio.save(_formulario.value)
            _formulario.value = FormularioModel()
            _guardadoCorrectamente.value = true
        }
        return true
    }
    fun ocultarMensajeGuardado() {
        _guardadoCorrectamente.value = false
    }
}