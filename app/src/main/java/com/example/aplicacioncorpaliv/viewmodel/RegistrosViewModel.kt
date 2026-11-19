package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.Contacto
import com.example.aplicacioncorpaliv.data.ContactoRepository
import com.example.aplicacioncorpaliv.data.Registro
import com.example.aplicacioncorpaliv.data.RegistroRepository
import com.example.aplicacioncorpaliv.data.remote.mensajeDeError
import com.example.aplicacioncorpaliv.model.CodigoSeguimiento
import com.example.aplicacioncorpaliv.model.Seguimiento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

// Resultado de consultar una solicitud con su código.
// EXPLICAR: 'sealed interface' = una lista cerrada de opciones posibles. La pantalla usa un 'when' para mostrar cada caso.
sealed interface ResultadoConsulta {
    data object SinConsultar : ResultadoConsulta
    data object CodigoInvalido : ResultadoConsulta
    data object NoEncontrada : ResultadoConsulta
    data object Consultando : ResultadoConsulta
    data class Error(val mensaje: String) : ResultadoConsulta
    data class Encontrada(val seguimiento: Seguimiento) : ResultadoConsulta
}

// AVANZADO: @JvmOverloads hace que Kotlin cree también el constructor que recibe SOLO 'application' (el que usa viewModel() en la app).
// Los demás parámetros tienen un valor por defecto, y en los tests unitarios se reemplazan por objetos falsos (MockK).
class RegistrosViewModel @JvmOverloads constructor(
    application: Application,
    private val repositorio: RegistroRepository = RegistroRepository(AppDatabase.getInstance(application).registroDao()),
    private val repositorioContactos: ContactoRepository = ContactoRepository(AppDatabase.getInstance(application).contactoDao())
) : AndroidViewModel(application) {

    val registros: Flow<List<Registro>> = repositorio.registros
    val contactos: Flow<List<Contacto>> = repositorioContactos.contactos

    private val _consulta = MutableStateFlow<ResultadoConsulta>(ResultadoConsulta.SinConsultar)
    val consulta: StateFlow<ResultadoConsulta> = _consulta.asStateFlow()

    private val _actualizando = MutableStateFlow(false)
    val actualizando: StateFlow<Boolean> = _actualizando.asStateFlow()
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    fun eliminar(registro: Registro) {
        viewModelScope.launch {
            repositorio.delete(registro)
        }
    }

    fun eliminarContacto(contacto: Contacto) {
        viewModelScope.launch {
            repositorioContactos.delete(contacto)
        }
    }

    // EXPLICAR: primero se revisa que el código tenga el formato correcto (COL- y 8 caracteres); recién entonces se
    // le pregunta al SERVIDOR. Si responde 404, el código no existe. Cualquier otro error se muestra como mensaje.
    fun consultarPorCodigo(codigo: String) {
        if (!CodigoSeguimiento.esValido(codigo)) {
            _consulta.value = ResultadoConsulta.CodigoInvalido
            return
        }
        viewModelScope.launch {
            _consulta.value = ResultadoConsulta.Consultando
            try {
                _consulta.value = ResultadoConsulta.Encontrada(repositorio.consultarSeguimiento(codigo))
            } catch (e: HttpException) {
                _consulta.value = if (e.code() == 404) {
                    ResultadoConsulta.NoEncontrada
                } else {
                    ResultadoConsulta.Error(mensajeDeError(e))
                }
            } catch (e: Exception) {
                _consulta.value = ResultadoConsulta.Error(mensajeDeError(e))
            }
        }
    }

    // EXPLICAR: le pregunta al servidor el estado actual de cada solicitud guardada en el teléfono y actualiza la copia local.
    // Se llama al abrir la pantalla y con el botón 'Actualizar estados'. Si no hay conexión, se siguen viendo los datos guardados.
    fun actualizar() {
        viewModelScope.launch {
            _actualizando.value = true
            _aviso.value = null
            try {
                repositorio.actualizarEstados()
            } catch (e: Exception) {
                _aviso.value = "No pudimos actualizar los estados. " + mensajeDeError(e)
            }
            _actualizando.value = false
        }
    }

    fun limpiarConsulta() {
        _consulta.value = ResultadoConsulta.SinConsultar
    }
}
