package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AppDatabase
import com.example.aplicacioncorpaliv.data.Contacto
import com.example.aplicacioncorpaliv.data.ContactoRepository
import com.example.aplicacioncorpaliv.data.Registro
import com.example.aplicacioncorpaliv.data.RegistroRepository
import com.example.aplicacioncorpaliv.model.CodigoSeguimiento
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Resultado de consultar una solicitud con su código.
// EXPLICAR: 'sealed interface' = una lista cerrada de opciones posibles. La pantalla usa un 'when' para mostrar cada caso.
sealed interface ResultadoConsulta {
    data object SinConsultar : ResultadoConsulta
    data object CodigoInvalido : ResultadoConsulta
    data object NoEncontrada : ResultadoConsulta
    data class Encontrada(val registro: Registro) : ResultadoConsulta
}

class RegistrosViewModel(application: Application) :
    AndroidViewModel(application) {

    private val baseDeDatos = AppDatabase.getInstance(application)

    private val repositorio = RegistroRepository(baseDeDatos.registroDao())
    private val repositorioContactos = ContactoRepository(baseDeDatos.contactoDao())

    val registros: Flow<List<Registro>> = repositorio.registros
    val contactos: Flow<List<Contacto>> = repositorioContactos.contactos

    private val _consulta = MutableStateFlow<ResultadoConsulta>(ResultadoConsulta.SinConsultar)
    val consulta: StateFlow<ResultadoConsulta> = _consulta.asStateFlow()

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

    // EXPLICAR: primero se revisa que el código tenga el formato correcto (COL- y 8 caracteres); recién entonces se busca.
    fun consultarPorCodigo(codigo: String) {
        if (!CodigoSeguimiento.esValido(codigo)) {
            _consulta.value = ResultadoConsulta.CodigoInvalido
            return
        }
        viewModelScope.launch {
            val encontrada = repositorio.buscarPorCodigo(codigo)
            _consulta.value = if (encontrada != null) {
                ResultadoConsulta.Encontrada(encontrada)
            } else {
                ResultadoConsulta.NoEncontrada
            }
        }
    }

    fun limpiarConsulta() {
        _consulta.value = ResultadoConsulta.SinConsultar
    }
}
