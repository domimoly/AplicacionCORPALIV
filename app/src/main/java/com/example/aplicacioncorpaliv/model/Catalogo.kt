package com.example.aplicacioncorpaliv.model

// Lo que devuelve ms-catalogo (mismos nombres que en el JSON del servidor)
data class Categoria(
    val id: Int,
    val nombre: String,
    val tipo: String
)

data class ProductoServicio(
    val id: Int,
    val categoriaId: Int,
    val nombre: String,
    val descripcion: String,
    val imagenUrl: String? = null,
    val precioReferencial: String? = null
)

// Lo que muestra la pantalla Productos: un producto ya listo, con el nombre de su categoría y su precio en texto.
// EXPLICAR: el servidor entrega la categoría como un número (categoriaId); el ViewModel la cambia por su nombre
// ("Bienestar", "Servicios"...) para que la pantalla no tenga que hacer esa cuenta.
data class ProductoItem(
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: String,
    val imagenUrl: String? = null
)
