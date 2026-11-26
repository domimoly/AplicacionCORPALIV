package com.example.aplicacioncorpaliv.model

// TIPOS DE COLABORACIÓN. Cada tipo tiene un CÓDIGO (el que se guarda y se enviará al backend) y un NOMBRE (el que ve la persona).
// EXPLICAR: los códigos son exactamente los de la Propuesta Técnica y los de ms-vinculacion (DONACION, ALIANZA_RSE...).
// Así, cuando se conecte el backend, no hay que traducir nada: se envía el mismo código que ya está guardado.
// EXPLICAR: 'descripcion' es una explicación corta de qué significa cada modalidad (la muestra el formulario).
data class TipoColaboracion(
    val codigo: String,
    val nombre: String,
    val descripcion: String = ""
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
        TipoColaboracion(
            DONACION, "Donación",
            "Tu aporte en dinero ayuda a financiar los programas de formación, la preparación para el empleo y la producción de Alba Lab."
        ),
        TipoColaboracion(
            VOLUNTARIADO, "Voluntariado",
            "Dedicas parte de tu tiempo y tus habilidades a apoyar actividades con personas con discapacidad múltiple."
        ),
        TipoColaboracion(
            ALIANZA_RSE, "Alianza empresarial",
            "Tu empresa se suma a la red de Alba Lab con iniciativas de responsabilidad social empresarial (RSE)."
        ),
        TipoColaboracion(
            COMPRA, "Compra de productos",
            "Compras productos elaborados en Alba Lab. Cada compra apoya la formación y el trabajo de jóvenes y adultos."
        ),
        TipoColaboracion(
            CAPACITACION, "Capacitación",
            "Tu equipo participa en charlas o talleres sobre inclusión y discapacidad múltiple."
        ),
        TipoColaboracion(
            OTRO, "Otro",
            "Cuéntanos tu idea y el equipo de vinculación te responderá."
        )
    )

    // Convierte un código en el nombre para mostrar. Si el código no existe, devuelve el mismo texto
    fun nombre(codigo: String): String = todos.firstOrNull { it.codigo == codigo }?.nombre ?: codigo
}
