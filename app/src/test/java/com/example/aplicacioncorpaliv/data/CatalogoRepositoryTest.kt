package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.data.remote.CatalogoApi
import com.example.aplicacioncorpaliv.model.Categoria
import com.example.aplicacioncorpaliv.model.ProductoServicio
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CatalogoRepositoryTest {

    private val api = mockk<CatalogoApi>()
    private val repositorio = CatalogoRepository(api)

    @Test
    fun `getProductos une cada producto con el nombre de su categoria`() = runTest {
        coEvery { api.getCategorias() } returns listOf(Categoria(1, "Alimentos", "PRODUCTO"))
        coEvery { api.getProductos() } returns listOf(
            ProductoServicio(10, 1, "Mermelada de prueba", "Casera", null, "$3.500")
        )

        val productos = repositorio.getProductos()

        assertEquals(1, productos.size)
        assertEquals("Alimentos", productos[0].categoria)
        assertEquals("$3.500", productos[0].precio)
    }

    @Test
    fun `usa Otros si la categoria no existe y A cotizar si no hay precio`() = runTest {
        coEvery { api.getCategorias() } returns listOf(Categoria(1, "Alimentos", "PRODUCTO"))
        coEvery { api.getProductos() } returns listOf(
            ProductoServicio(11, 99, "Taller de prueba", "Curso", null, null),
            ProductoServicio(12, 1, "Producto sin precio", "Detalle", null, "   ")
        )

        val productos = repositorio.getProductos()

        assertEquals("Otros", productos[0].categoria)
        assertEquals("A cotizar", productos[0].precio)
        assertEquals("A cotizar", productos[1].precio)
    }
}
