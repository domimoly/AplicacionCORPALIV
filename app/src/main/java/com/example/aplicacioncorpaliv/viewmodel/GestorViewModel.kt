package com.example.aplicacioncorpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.GestorRepository
import com.example.aplicacioncorpaliv.data.remote.mensajeDeError
import com.example.aplicacioncorpaliv.model.EstadoSolicitud
import com.example.aplicacioncorpaliv.model.Solicitud
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

// VIEWMODEL DEL GESTOR: maneja el inicio de sesión y las acciones del panel de gestión.
// EXPLICAR: al iniciar sesión, el servidor (ms-admin) entrega un TOKEN. El ViewModel lo guarda en una variable PRIVADA
// y lo envía en cada petición del gestor. 'gestorActual' es null mientras nadie ha iniciado sesión: la pantalla mira ese
// valor y muestra el login o el panel. La sesión vive en memoria: se mantiene al girar el teléfono, pero se pierde al cerrar la app.
// AVANZADO: ya no es AndroidViewModel porque no necesita el contexto: las solicitudes se piden al servidor, no a Room.
class GestorViewModel(
    private val repositorio: GestorRepository = GestorRepository()
) : ViewModel() {

    // El token JWT. No es un StateFlow porque la pantalla no necesita verlo, solo se usa para llamar al servidor
    private var token: String? = null

    private val _gestorActual = MutableStateFlow<String?>(null)
    val gestorActual: StateFlow<String?> = _gestorActual.asStateFlow()

    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    // true mientras se espera la respuesta del servidor (la pantalla desactiva el botón y muestra una ruedita)
    private val _iniciando = MutableStateFlow(false)
    val iniciando: StateFlow<Boolean> = _iniciando.asStateFlow()

    // Todas las solicitudes que entrega el servidor
    private val _solicitudes = MutableStateFlow<List<Solicitud>>(emptyList())
    val solicitudes: StateFlow<List<Solicitud>> = _solicitudes.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Mensaje corto sobre la última acción (por ejemplo 'Estado actualizado' o el error del servidor)
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    // Primero se validan los campos (igual que en el formulario) y luego se le pregunta al servidor
    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _errorLogin.value = "Ingresa tu correo y tu clave"
            return
        }
        if (!correo.contains("@")) {
            _errorLogin.value = "Ingresa un correo válido"
            return
        }
        viewModelScope.launch {
            _iniciando.value = true
            _errorLogin.value = null
            try {
                val respuesta = repositorio.iniciarSesion(correo, clave)
                token = respuesta.token
                _gestorActual.value = respuesta.nombre
                cargar()
            } catch (e: HttpException) {
                // El servidor responde 401 cuando el correo o la clave no coinciden
                _errorLogin.value = if (e.code() == 401) "Correo o clave incorrectos" else mensajeDeError(e)
            } catch (e: Exception) {
                _errorLogin.value = mensajeDeError(e)
            }
            _iniciando.value = false
        }
    }

    fun cerrarSesion() {
        token = null
        _gestorActual.value = null
        _errorLogin.value = null
        _solicitudes.value = emptyList()
        _aviso.value = null
    }

    // Pide al servidor la lista de solicitudes (GET /solicitudes con el token)
    fun cargar() {
        val tokenActual = token ?: return
        viewModelScope.launch {
            _cargando.value = true
            try {
                _solicitudes.value = repositorio.listar(tokenActual)
            } catch (e: Exception) {
                manejarError(e)
            }
            _cargando.value = false
        }
    }

    // Cambiar el estado de una solicitud (Update). viewModelScope.launch ejecuta la tarea sin congelar la pantalla
    // EXPLICAR: el servidor devuelve la solicitud ya actualizada y se reemplaza en la lista. Si el servidor la rechaza
    // (por ejemplo, una solicitud CONCRETADO ya no se puede modificar) se muestra su mensaje.
    fun cambiarEstado(solicitud: Solicitud, estado: String, observacion: String) {
        val tokenActual = token ?: return
        viewModelScope.launch {
            try {
                val actualizada = repositorio.cambiarEstado(tokenActual, solicitud.id, estado, observacion)
                _solicitudes.value = _solicitudes.value.map { if (it.id == actualizada.id) actualizada else it }
                _aviso.value = "Estado actualizado: ${EstadoSolicitud.texto(estado)}."
            } catch (e: Exception) {
                manejarError(e)
            }
        }
    }

    // Eliminar una solicitud (Delete)
    fun eliminar(solicitud: Solicitud) {
        val tokenActual = token ?: return
        viewModelScope.launch {
            try {
                repositorio.eliminar(tokenActual, solicitud.id)
                _solicitudes.value = _solicitudes.value.filter { it.id != solicitud.id }
                _aviso.value = "Solicitud eliminada."
            } catch (e: Exception) {
                manejarError(e)
            }
        }
    }

    // EXPLICAR: si el servidor responde 401, el token venció (dura 8 horas): se cierra la sesión para volver a ingresar
    private fun manejarError(e: Exception) {
        if (e is HttpException && e.code() == 401) {
            cerrarSesion()
            _errorLogin.value = mensajeDeError(e)
        } else {
            _aviso.value = mensajeDeError(e)
        }
    }

    fun limpiarAviso() {
        _aviso.value = null
    }
}
