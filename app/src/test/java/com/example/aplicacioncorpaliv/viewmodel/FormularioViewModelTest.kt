package com.example.aplicacioncorpaliv.viewmodel

import android.app.Application
import com.example.aplicacioncorpaliv.MainDispatcherRule
import com.example.aplicacioncorpaliv.data.PerfilDataStore
import com.example.aplicacioncorpaliv.data.RegistroRepository
import com.example.aplicacioncorpaliv.model.FormularioModel
import com.example.aplicacioncorpaliv.model.TiposColaboracion
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.io.IOException
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

// EXPLICAR: Application, el repositorio y PerfilDataStore son 'mocks' (falsos): así se prueba solo la lógica del ViewModel.
class FormularioViewModelTest {

    @get:Rule
    val reglaMain = MainDispatcherRule()

    private val aplicacion = mockk<Application>(relaxed = true)
    private val repositorio = mockk<RegistroRepository>()
    private val perfil = mockk<PerfilDataStore>()

    private fun crearViewModel() = FormularioViewModel(aplicacion, repositorio, perfil)

    // Llena el formulario con datos válidos (todos inventados)
    private fun llenarFormulario(viewModel: FormularioViewModel, categoria: String = TiposColaboracion.VOLUNTARIADO) {
        viewModel.actualizarNombre("Ana Prueba")
        viewModel.actualizarMedioContacto("ana@ejemplo.cl")
        viewModel.actualizarDescripcion("Quiero ayudar con talleres")
        viewModel.actualizarCategoria(categoria)
        viewModel.actualizarAceptaTerminos(true)
    }

    @Test
    fun `al comienzo el formulario esta vacio y sin errores`() {
        val viewModel = crearViewModel()

        assertEquals(FormularioModel(), viewModel.formulario.value)
        assertEquals(FormularioErrores(), viewModel.errores.value)
        assertFalse(viewModel.guardadoCorrectamente.value)
        assertNull(viewModel.codigoGenerado.value)
    }

    @Test
    fun `los metodos actualizar cambian cada campo`() {
        val viewModel = crearViewModel()

        llenarFormulario(viewModel, TiposColaboracion.DONACION)
        viewModel.actualizarActivo(true)
        viewModel.actualizarFecha(1_700_000_000_000L)

        val formulario = viewModel.formulario.value
        assertEquals("Ana Prueba", formulario.nombre)
        assertEquals("ana@ejemplo.cl", formulario.medioContacto)
        assertEquals("Quiero ayudar con talleres", formulario.descripcion)
        assertEquals(TiposColaboracion.DONACION, formulario.categoria)
        assertTrue(formulario.aceptaTerminos)
        assertTrue(formulario.activo)
        assertEquals(1_700_000_000_000L, formulario.fecha)
    }

    @Test
    fun `la cantidad solo acepta digitos y hasta 9 caracteres`() {
        val viewModel = crearViewModel()

        viewModel.actualizarCantidad("5000")
        viewModel.actualizarCantidad("50a")          // tiene una letra: se ignora
        viewModel.actualizarCantidad("1234567890")   // 10 dígitos: se ignora

        assertEquals("5000", viewModel.formulario.value.cantidad)
    }

    @Test
    fun `un formulario vacio no es valido y muestra los errores`() {
        val viewModel = crearViewModel()

        val valido = viewModel.validarFormulario()

        val errores = viewModel.errores.value
        assertFalse(valido)
        assertEquals("El nombre es obligatorio", errores.nombre)
        assertEquals("El correo o teléfono es obligatorio", errores.medioContacto)
        assertEquals("El mensaje es obligatorio", errores.descripcion)
        assertNotNull(errores.aceptaTerminos)
        assertEquals("Debe seleccionar un tipo de colaboración", errores.categoria)
        assertNull(errores.cantidad) // el monto solo se exige en las donaciones
    }

    @Test
    fun `en una donacion el monto es obligatorio y tiene un minimo`() {
        val viewModel = crearViewModel()
        llenarFormulario(viewModel, TiposColaboracion.DONACION)

        assertFalse(viewModel.validarFormulario())
        assertEquals("Debe ingresar un número", viewModel.errores.value.cantidad)

        viewModel.actualizarCantidad("500")
        assertFalse(viewModel.validarFormulario())
        assertEquals("El monto mínimo es de 1000 pesos", viewModel.errores.value.cantidad)

        viewModel.actualizarCantidad("5000")
        assertTrue(viewModel.validarFormulario())
    }

    @Test
    fun `en una compra el producto es obligatorio`() {
        val viewModel = crearViewModel()
        llenarFormulario(viewModel, TiposColaboracion.COMPRA)

        assertFalse(viewModel.validarFormulario())
        assertEquals("Elige el producto o servicio que te interesa", viewModel.errores.value.producto)

        viewModel.actualizarProducto(3)
        assertTrue(viewModel.validarFormulario())
        assertNull(viewModel.errores.value.producto)
    }

    @Test
    fun `en los otros tipos el producto no se exige y se borra al cambiar de tipo`() {
        val viewModel = crearViewModel()
        llenarFormulario(viewModel, TiposColaboracion.VOLUNTARIADO)

        // Voluntariado no necesita producto: el formulario es válido sin elegir ninguno
        assertTrue(viewModel.validarFormulario())
        assertNull(viewModel.errores.value.producto)

        // Si se elige un producto en 'Compra' y luego se cambia de tipo, el producto se borra
        viewModel.actualizarCategoria(TiposColaboracion.COMPRA)
        viewModel.actualizarProducto(3)
        assertEquals(3, viewModel.formulario.value.productoId)
        viewModel.actualizarCategoria(TiposColaboracion.VOLUNTARIADO)
        assertNull(viewModel.formulario.value.productoId)
    }

    @Test
    fun `guardar con datos invalidos no llama al servidor`() {
        val viewModel = crearViewModel()

        val resultado = viewModel.guardar()

        assertFalse(resultado)
        coVerify(exactly = 0) { repositorio.save(any()) }
    }

    @Test
    fun `guardar correcto vacia el formulario y entrega el codigo de seguimiento`() {
        coEvery { repositorio.save(any()) } returns "COL-AAAA2222"
        val viewModel = crearViewModel()
        llenarFormulario(viewModel)

        val resultado = viewModel.guardar()

        assertTrue(resultado)
        assertEquals("COL-AAAA2222", viewModel.codigoGenerado.value)
        assertTrue(viewModel.guardadoCorrectamente.value)
        assertEquals(FormularioModel(), viewModel.formulario.value)
        assertNull(viewModel.errorEnvio.value)
        assertFalse(viewModel.enviando.value)
    }

    @Test
    fun `si el servidor falla guarda el error y conserva lo escrito`() {
        coEvery { repositorio.save(any()) } throws IOException("sin conexión")
        val viewModel = crearViewModel()
        llenarFormulario(viewModel)

        viewModel.guardar()

        assertTrue(viewModel.errorEnvio.value!!.startsWith("No hay conexión"))
        assertFalse(viewModel.guardadoCorrectamente.value)
        assertNull(viewModel.codigoGenerado.value)
        assertEquals("Ana Prueba", viewModel.formulario.value.nombre)
        assertFalse(viewModel.enviando.value)
    }

    @Test
    fun `cerrarCodigo cerrarErrorEnvio y ocultarMensajeGuardado limpian los avisos`() {
        coEvery { repositorio.save(any()) } returns "COL-AAAA2222"
        val viewModel = crearViewModel()
        llenarFormulario(viewModel)
        viewModel.guardar()

        viewModel.cerrarCodigo()
        viewModel.ocultarMensajeGuardado()
        viewModel.cerrarErrorEnvio()

        assertNull(viewModel.codigoGenerado.value)
        assertFalse(viewModel.guardadoCorrectamente.value)
        assertNull(viewModel.errorEnvio.value)
    }

    @Test
    fun `precargarDesdePerfil completa solo los campos vacios`() {
        every { perfil.nombre } returns flowOf("Nombre del perfil")
        every { perfil.medioContacto } returns flowOf("perfil@ejemplo.cl")
        val viewModel = crearViewModel()
        viewModel.actualizarNombre("Ana Prueba") // ya escrito: no se debe pisar

        viewModel.precargarDesdePerfil()

        assertEquals("Ana Prueba", viewModel.formulario.value.nombre)
        assertEquals("perfil@ejemplo.cl", viewModel.formulario.value.medioContacto)
    }
}
