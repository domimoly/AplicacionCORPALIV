package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.GestorRepository
import com.example.aplicacioncorpaliv.data.Registro
import com.example.aplicacioncorpaliv.data.RegistroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// VIEWMODEL DEL GESTOR: maneja el inicio de sesión y las acciones del panel de gestión.
// EXPLICAR: 'gestorActual' es null mientras nadie ha iniciado sesión; cuando el login es correcto guarda el nombre.
// La pantalla mira ese valor: si es null muestra el formulario de login, y si no, el panel.
// La sesión vive en este ViewModel (en memoria): se mantiene al girar el teléfono, pero se pierde al cerrar la app.
class GestorViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorioRegistros = RegistroRepository(AppDatabase.getInstance(application).registroDao())
    private val repositorioGestor = GestorRepository()

    // Todas las solicitudes (la lista se actualiza sola cuando cambia la base de datos)
    val registros: Flow<List<Registro>> = repositorioRegistros.registros

    private val _gestorActual = MutableStateFlow<String?>(null)
    val gestorActual: StateFlow<String?> = _gestorActual.asStateFlow()

    private val _errorLogin = MutableStateFlow<String?>(null)
    val errorLogin: StateFlow<String?> = _errorLogin.asStateFlow()

    // Primero se validan los campos (igual que en el formulario) y luego se consulta al repositorio
    fun iniciarSesion(correo: String, clave: String) {
        if (correo.isBlank() || clave.isBlank()) {
            _errorLogin.value = "Ingresa tu correo y tu clave"
            return
        }
        if (!correo.contains("@")) {
            _errorLogin.value = "Ingresa un correo válido"
            return
        }
        val nombre = repositorioGestor.iniciarSesion(correo, clave)
        if (nombre == null) {
            _errorLogin.value = "Correo o clave incorrectos"
        } else {
            _errorLogin.value = null
            _gestorActual.value = nombre
        }
    }

    fun cerrarSesion() {
        _gestorActual.value = null
        _errorLogin.value = null
    }

    // Cambiar el estado de una solicitud (Update). viewModelScope.launch ejecuta la tarea sin congelar la pantalla
    fun cambiarEstado(registro: Registro, estado: String, observacion: String) {
        viewModelScope.launch {
            repositorioRegistros.cambiarEstado(registro, estado, observacion)
        }
    }

    // Eliminar una solicitud (Delete)
    fun eliminar(registro: Registro) {
        viewModelScope.launch {
            repositorioRegistros.delete(registro)
        }
    }
}
