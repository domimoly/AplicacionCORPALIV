package com.example.aplicacioncorpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.ContenidosRepository
import com.example.aplicacioncorpaliv.data.DatosDeEjemplo
import com.example.aplicacioncorpaliv.model.Programa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Entrega a la pantalla Programas la lista que viene del microservicio ms-contenidos.
// EXPLICAR: tiene 3 estados que la pantalla observa: la lista, si está cargando y si hubo un error.
// Si la conexión falla (try/catch), muestra los datos de respaldo y avisa con un mensaje; la pantalla ofrece 'Reintentar'.
class ProgramasViewModel(
    private val repositorio: ContenidosRepository = ContenidosRepository()
) : ViewModel() {

    private val _programas = MutableStateFlow<List<Programa>>(emptyList())
    val programas: StateFlow<List<Programa>> = _programas.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // init se ejecuta una vez al crear el ViewModel: pide los datos apenas se abre la pantalla
    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                _programas.value = repositorio.getProgramas()
            } catch (e: Exception) {
                _programas.value = DatosDeEjemplo.programas
                _error.value = "No pudimos conectar con el servidor. Mostrando información guardada en la app."
            } finally {
                _cargando.value = false
            }
        }
    }
}
