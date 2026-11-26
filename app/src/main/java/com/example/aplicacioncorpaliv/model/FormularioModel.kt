package com.example.aplicacioncorpaliv.model

// Los datos que la persona va escribiendo en el formulario Colaborar.
// EXPLICAR: 'categoria' guarda el CÓDIGO del tipo (por ejemplo 'DONACION'); el nombre bonito se obtiene con TiposColaboracion.nombre().
// 'medioContacto' es el correo o teléfono para que CORPALIV pueda responder.
data class FormularioModel(
    val nombre: String = "",
    val medioContacto: String = "",
    val descripcion: String = "",
    val cantidad: String = "",
    val aceptaTerminos: Boolean = false,
    val activo: Boolean = false,
    val fecha: Long? = null,
    val categoria: String = "",
    // EXPLICAR: número (id) del producto elegido en 'Compra de productos'. null = todavía no se elige ninguno
    val productoId: Int? = null
)
