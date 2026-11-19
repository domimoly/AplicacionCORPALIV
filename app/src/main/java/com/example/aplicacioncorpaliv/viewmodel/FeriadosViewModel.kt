package com.example.aplicacioncorpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.FeriadosRepository
import com.example.aplicacioncorpaliv.model.Feriado
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// VIEWMODEL de la API externa. Guarda 3 cosas que la pantalla observa: los 'feriados', si está 'cargando' y un 'error'.
// EXPLICAR: pide los datos al abrirse (init) y también cuando se toca 'Reintentar'. Si algo falla, solo se guarda un mensaje:
// la app no se cierra y el resto de la pantalla sigue funcionando.
class FeriadosViewModel(
    private val repositorio: FeriadosRepository = FeriadosRepository()
) : ViewModel() {

    // null = todavía no llegaron los datos. Lista vacía = llegaron, pero ya no quedan feriados este año
    private val _feriados = MutableStateFlow<List<Feriado>?>(null)
    val feriados: StateFlow<List<Feriado>?> = _feriados.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            try {
                val hoy = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                _feriados.value = repositorio.proximos(hoy)
            } catch (e: Exception) {
                _error.value = "No pudimos obtener los feriados. Revisa tu conexión a internet."
            }
            _cargando.value = false
        }
    }
}
