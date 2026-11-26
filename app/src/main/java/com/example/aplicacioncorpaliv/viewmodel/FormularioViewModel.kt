package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.PerfilDataStore
import com.example.aplicacioncorpaliv.data.RegistroRepository
import com.example.aplicacioncorpaliv.data.remote.mensajeDeError
import com.example.aplicacioncorpaliv.model.FormularioModel
import com.example.aplicacioncorpaliv.model.TiposColaboracion
import com.example.aplicacioncorpaliv.model.Validaciones
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Un mensaje de error por campo (null = ese campo está bien).
// EXPLICAR: la fecha ya no aparece aquí porque ahora es opcional (no se puede escribir mal algo que no es obligatorio).
data class FormularioErrores(
    val nombre: String? = null,
    val medioContacto: String? = null,
    val descripcion: String? = null,
    val cantidad: String? = null,
    val aceptaTerminos: String? = null,
    val categoria: String? = null,
    val producto: String? = null
)

// AVANZADO: @JvmOverloads hace que Kotlin cree también el constructor que recibe SOLO 'application' (el que usa viewModel() en la app).
// Los demás parámetros tienen un valor por defecto, y en los tests unitarios se reemplazan por objetos falsos (MockK).
class FormularioViewModel @JvmOverloads constructor(
    application: Application,
    private val repositorio: RegistroRepository = RegistroRepository(AppDatabase.getInstance(application).registroDao()),
    private val perfil: PerfilDataStore = PerfilDataStore(application)
) : AndroidViewModel(application) {

    private val _formulario = MutableStateFlow(FormularioModel())
    val formulario: StateFlow<FormularioModel> = _formulario.asStateFlow()
    private val _errores = MutableStateFlow(FormularioErrores())
    val errores: StateFlow<FormularioErrores> = _errores.asStateFlow()
    private val _guardadoCorrectamente = MutableStateFlow(false)
    val guardadoCorrectamente: StateFlow<Boolean> = _guardadoCorrectamente.asStateFlow()

    // EXPLICAR: 'enviando' es true mientras se espera la respuesta del servidor (la pantalla muestra una ruedita).
    // 'errorEnvio' guarda el mensaje si el servidor no respondió o rechazó los datos.
    private val _enviando = MutableStateFlow(false)
    val enviando: StateFlow<Boolean> = _enviando.asStateFlow()
    private val _errorEnvio = MutableStateFlow<String?>(null)
    val errorEnvio: StateFlow<String?> = _errorEnvio.asStateFlow()

    // Código de seguimiento de la última solicitud enviada. Se muestra en pantalla hasta que la persona lo cierre
    private val _codigoGenerado = MutableStateFlow<String?>(null)
    val codigoGenerado: StateFlow<String?> = _codigoGenerado.asStateFlow()

    fun actualizarNombre(valor: String) {
        _formulario.value = _formulario.value.copy(nombre = valor)
    }
    fun actualizarMedioContacto(valor: String) {
        _formulario.value = _formulario.value.copy(medioContacto = valor)
    }
    fun actualizarDescripcion(valor: String) {
        _formulario.value =_formulario.value.copy(descripcion = valor)
    }
    // Solo se aceptan dígitos y hasta 9 (más de eso no cabe en un número Int)
    fun actualizarCantidad(valor: String) {
        if (valor.all { it.isDigit() } && valor.length <= 9) {
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
    // 'valor' es el CÓDIGO del tipo (por ejemplo 'DONACION')
    // EXPLICAR: el producto solo se usa en 'Compra de productos'. Si la persona cambia a otro tipo, se borra el producto
    // que había elegido, para no enviar un dato que ya no corresponde.
    fun actualizarCategoria(valor: String) {
        val actual = _formulario.value
        _formulario.value = actual.copy(
            categoria = valor,
            productoId = if (valor == TiposColaboracion.COMPRA) actual.productoId else null
        )
    }
    // 'valor' es el número (id) del producto elegido en la lista desplegable
    fun actualizarProducto(valor: Int?) {
        _formulario.value = _formulario.value.copy(productoId = valor)
    }

    // EXPLICAR: trae el nombre y el contacto guardados en 'Perfil' y los escribe en el formulario,
    // pero SOLO en los campos que están vacíos (así no se pisa lo que la persona ya escribió).
    fun precargarDesdePerfil() {
        viewModelScope.launch {
            val nombrePerfil = perfil.nombre.first()
            val contactoPerfil = perfil.medioContacto.first()
            val actual = _formulario.value
            _formulario.value = actual.copy(
                nombre = actual.nombre.ifBlank { nombrePerfil },
                medioContacto = actual.medioContacto.ifBlank { contactoPerfil }
            )
        }
    }

    // Reglas de validacion | Las reglas están en model/Validaciones.kt. Para cambiar una regla se edita ese archivo
    fun validarFormulario(): Boolean {
        val datos = _formulario.value
        val esDonacion = datos.categoria == TiposColaboracion.DONACION
        val esCompra = datos.categoria == TiposColaboracion.COMPRA

        val nuevosErrores = FormularioErrores(
            nombre = Validaciones.nombre(datos.nombre),
            medioContacto = Validaciones.medioContacto(datos.medioContacto),
            descripcion = Validaciones.mensaje(datos.descripcion),
            // El monto solo se exige cuando el tipo es Donación
            cantidad = Validaciones.monto(datos.cantidad, obligatorio = esDonacion),
            aceptaTerminos = if (datos.aceptaTerminos) null else "Debes aceptar la política de privacidad para enviar",
            categoria = if (datos.categoria.isBlank()) "Debe seleccionar un tipo de colaboración" else null,
            // El producto solo se exige cuando el tipo es Compra de productos
            producto = Validaciones.producto(datos.productoId, obligatorio = esCompra)
        )
        _errores.value = nuevosErrores
        return nuevosErrores == FormularioErrores()
    }
    // Si todo es válido | ENVÍA la solicitud al servidor. Solo si el servidor la acepta: vacía el formulario,
    // guarda el código de seguimiento que entregó el servidor y avisa con guardadoCorrectamente.
    // EXPLICAR: try/catch atrapa los errores (servidor apagado, datos rechazados) para que la app no se cierre sola.
    fun guardar(): Boolean {
        if (!validarFormulario()) {
            return false
        }
        viewModelScope.launch {
            _enviando.value = true
            _errorEnvio.value = null
            try {
                val codigo = repositorio.save(_formulario.value)
                _formulario.value = FormularioModel()
                _codigoGenerado.value = codigo
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
    fun cerrarCodigo() {
        _codigoGenerado.value = null
    }
}
