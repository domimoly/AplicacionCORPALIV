package com.example.aplicacioncorpaliv.viewmodel

import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.CatalogoRepository
import com.example.aplicacioncorpaliv.data.DatosDeEjemplo
import com.example.aplicacioncorpaliv.model.ProductoItem
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ProductosViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val repositorio = mockk<CatalogoRepository>()
    private val productos = listOf(ProductoItem("Mermelada de prueba", "Alimentos", "Casera", "$3.500"))

    @Test
    fun `al crearse carga el catalogo del servidor`() {
        coEvery { repositorio.getProductos() } returns productos

        val viewModel = ProductosViewModel(repositorio)

        assertEquals(productos, viewModel.productos.value)
        assertNull(viewModel.error.value)
        assertFalse(viewModel.cargando.value)
    }

    @Test
    fun `si el servidor falla muestra el catalogo de ejemplo y un mensaje`() {
        coEvery { repositorio.getProductos() } throws IOException("sin conexión")

        val viewModel = ProductosViewModel(repositorio)

        assertEquals(DatosDeEjemplo.productos, viewModel.productos.value)
        assertNotNull(viewModel.error.value)
    }

    @Test
    fun `reintentar carga el catalogo cuando el servidor vuelve`() {
        coEvery { repositorio.getProductos() } throws IOException("sin conexión")
        val viewModel = ProductosViewModel(repositorio)

        coEvery { repositorio.getProductos() } returns productos
        viewModel.cargar()

        assertNull(viewModel.error.value)
        assertEquals(productos, viewModel.productos.value)
    }
}
