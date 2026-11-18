package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.AccesibilidadDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// EXPLICAR: este ViewModel guarda las opciones de accesibilidad. Lo usan DOS lugares:
// MainActivity (para aplicar el tamaño de texto y el contraste a TODA la app) y la pantalla Accesibilidad
// (para cambiarlas). Los dos reciben el MISMO objeto, porque viewModel() entrega uno por Activity.
class AccesibilidadViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repositorio = AccesibilidadDataStore(application)

    private val _nivelTexto = MutableStateFlow(0)
    val nivelTexto = _nivelTexto.asStateFlow()

    private val _altoContraste = MutableStateFlow(false)
    val altoContraste = _altoContraste.asStateFlow()

    // Si los botones 'Escuchar' se muestran o no (por defecto sí)
    private val _lecturaVoz = MutableStateFlow(true)
    val lecturaVoz = _lecturaVoz.asStateFlow()

    // AVANZADO: al iniciar se "escucha" lo guardado con collect. Cada vez que el valor guardado cambia
    // (por ejemplo al presionar una opción), collect lo recibe y actualiza el estado que mira la pantalla.
    init {
        viewModelScope.launch {
            repositorio.nivelTexto.collect { _nivelTexto.value = it }
        }
        viewModelScope.launch {
            repositorio.altoContraste.collect { _altoContraste.value = it }
        }
        viewModelScope.launch {
            repositorio.lecturaVoz.collect { _lecturaVoz.value = it }
        }
    }

    fun cambiarNivelTexto(nivel: Int) {
        viewModelScope.launch {
            repositorio.guardarNivelTexto(nivel)
        }
    }

    fun cambiarAltoContraste(activado: Boolean) {
        viewModelScope.launch {
            repositorio.guardarAltoContraste(activado)
        }
    }

    fun cambiarLecturaVoz(activado: Boolean) {
        viewModelScope.launch {
            repositorio.guardarLecturaVoz(activado)
        }
    }

    // Vuelve todo a como estaba al instalar la app
    fun restablecer() {
        viewModelScope.launch {
            repositorio.guardarNivelTexto(0)
            repositorio.guardarAltoContraste(false)
            repositorio.guardarLecturaVoz(true)
        }
    }
}
