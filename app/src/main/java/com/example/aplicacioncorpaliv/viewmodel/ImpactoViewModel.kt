package com.example.aplicacioncorpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.ContenidosRepository
import com.example.aplicacioncorpaliv.data.DatosDeEjemplo
import com.example.aplicacioncorpaliv.model.HistoriaImpacto
import com.example.aplicacioncorpaliv.model.IndicadorImpacto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// EXPLICAR: entrega a la pantalla Impacto los indicadores y las historias que vienen de ms-contenidos.
// Si la conexión falla, usa los datos de ejemplo de DatosDeEjemplo (ficticios y anonimizados).
// Es un ViewModel (y no AndroidViewModel) porque no necesita el contexto de la app: no guarda nada en el teléfono.
class ImpactoViewModel(
    private val repositorio: ContenidosRepository = ContenidosRepository()
) : ViewModel() {

    private val _indicadores = MutableStateFlow<List<IndicadorImpacto>>(emptyList())
    val indicadores: StateFlow<List<IndicadorImpacto>> = _indicadores.asStateFlow()

    private val _historias = MutableStateFlow<List<HistoriaImpacto>>(emptyList())
    val historias: StateFlow<List<HistoriaImpacto>> = _historias.asStateFlow()

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
                _indicadores.value = repositorio.getIndicadores()
                _historias.value = repositorio.getHistorias()
            } catch (e: Exception) {
                _indicadores.value = DatosDeEjemplo.indicadores
                _historias.value = DatosDeEjemplo.historias
                _error.value = "No pudimos conectar con el servidor. Mostrando datos de ejemplo guardados en la app."
            } finally {
                _cargando.value = false
            }
        }
    }
}
