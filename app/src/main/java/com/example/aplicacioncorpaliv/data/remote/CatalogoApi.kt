package com.example.aplicacioncorpaliv.data.remote

import com.example.aplicacioncorpaliv.model.Categoria
import com.example.aplicacioncorpaliv.model.ProductoServicio
import retrofit2.http.GET

// Endpoints públicos de ms-catalogo (puerto 9094)
interface CatalogoApi {
    @GET("productos")
    suspend fun getProductos(): List<ProductoServicio>

    @GET("categorias")
    suspend fun getCategorias(): List<Categoria>
}
