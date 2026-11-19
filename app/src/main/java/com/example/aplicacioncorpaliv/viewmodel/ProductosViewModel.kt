package com.example.aplicacioncorpaliv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacioncorpaliv.data.CatalogoRepository
import com.example.aplicacioncorpaliv.data.DatosDeEjemplo
import com.example.aplicacioncorpaliv.model.ProductoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Entrega a la pantalla Productos el catálogo del microservicio ms-catalogo (mismo patrón que ProgramasViewModel).
class ProductosViewModel(
    private val repositorio: CatalogoRepository = CatalogoRepository()
) : ViewModel() {

    private val _productos = MutableStateFlow<List<ProductoItem>>(emptyList())
    val productos: StateFlow<List<ProductoItem>> = _productos.asStateFlow()

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
                _productos.value = repositorio.getProductos()
            } catch (e: Exception) {
                _productos.value = DatosDeEjemplo.productos
                _error.value = "No pudimos conectar con el servidor. Mostrando el catálogo guardado en la app."
            } finally {
                _cargando.value = false
            }
        }
    }
}
