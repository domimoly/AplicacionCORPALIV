package com.example.aplicacioncorpaliv.model

// TIPOS DE COLABORACIÓN. Cada tipo tiene un CÓDIGO (el que se guarda y se enviará al backend) y un NOMBRE (el que ve la persona).
// EXPLICAR: los códigos son exactamente los de la Propuesta Técnica y los de ms-vinculacion (DONACION, ALIANZA_RSE...).
// Así, cuando se conecte el backend, no hay que traducir nada: se envía el mismo código que ya está guardado.
data class TipoColaboracion(
    val codigo: String,
    val nombre: String
)

object TiposColaboracion {
    const val DONACION = "DONACION"
    const val VOLUNTARIADO = "VOLUNTARIADO"
    const val ALIANZA_RSE = "ALIANZA_RSE"
    const val COMPRA = "COMPRA"
    const val CAPACITACION = "CAPACITACION"
    const val OTRO = "OTRO"

    // Lista en el orden en que aparece en el menú desplegable del formulario
    val todos = listOf(
        TipoColaboracion(DONACION, "Donación"),
        TipoColaboracion(VOLUNTARIADO, "Voluntariado"),
        TipoColaboracion(ALIANZA_RSE, "Alianza empresarial"),
        TipoColaboracion(COMPRA, "Compra de productos"),
        TipoColaboracion(CAPACITACION, "Capacitación"),
        TipoColaboracion(OTRO, "Otro")
    )

    // Convierte un código en el nombre para mostrar. Si el código no existe, devuelve el mismo texto
    fun nombre(codigo: String): String = todos.firstOrNull { it.codigo == codigo }?.nombre ?: codigo
}
