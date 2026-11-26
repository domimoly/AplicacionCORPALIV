package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.CatalogoApi
import com.example.aplicacioncorpaliv.data.remote.RetrofitInstance
import com.example.aplicacioncorpaliv.model.ProductoItem

// Intermediario con el microservicio ms-catalogo.
class CatalogoRepository(
    private val api: CatalogoApi = RetrofitInstance.catalogo
) {
    // EXPLICAR: se piden las categorías y los productos, y se unen. El servidor entrega en cada producto
    // solo el número de su categoría (categoriaId); aquí se busca el nombre con ese número.
    suspend fun getProductos(): List<ProductoItem> {
        val nombresDeCategorias = api.getCategorias().associate { it.id to it.nombre }
        return api.getProductos().map { producto ->
            ProductoItem(
                id = producto.id,
                nombre = producto.nombre,
                categoria = nombresDeCategorias[producto.categoriaId] ?: "Otros",
                descripcion = producto.descripcion,
                // Si el producto no tiene precio, se muestra 'A cotizar'
                precio = producto.precioReferencial?.takeIf { it.isNotBlank() } ?: "A cotizar",
                imagenUrl = producto.imagenUrl
            )
        }
    }
}
