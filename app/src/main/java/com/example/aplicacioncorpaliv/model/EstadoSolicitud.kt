package com.example.aplicacioncorpaliv.model

// ESTADOS de una solicitud de colaboración. Los códigos (RECIBIDA, EN_REVISION...) son los de la Propuesta Técnica,
// así que cuando se conecte el backend (ms-vinculacion) serán exactamente los mismos.
// EXPLICAR: una solicitud nace RECIBIDA y el gestor de vinculación la va cambiando de estado.
// Se guarda el CÓDIGO (texto en mayúsculas) en la base de datos y, para mostrarlo a la persona, se usa texto() o descripcion().
object EstadoSolicitud {
    const val RECIBIDA = "RECIBIDA"
    const val EN_REVISION = "EN_REVISION"
    const val CONTACTADO = "CONTACTADO"
    const val CONCRETADO = "CONCRETADO"
    const val DESCARTADO = "DESCARTADO"

    // Todos los estados en orden, para armar listas de opciones
    val todos = listOf(RECIBIDA, EN_REVISION, CONTACTADO, CONCRETADO, DESCARTADO)

    // Nombre corto para mostrar en la etiqueta de color
    fun texto(estado: String): String = when (estado) {
        RECIBIDA -> "Recibida"
        EN_REVISION -> "En revisión"
        CONTACTADO -> "Contactado"
        CONCRETADO -> "Concretado"
        DESCARTADO -> "Descartado"
        else -> estado
    }

    // Explicación en palabras simples para quien envió la solicitud
    fun descripcion(estado: String): String = when (estado) {
        RECIBIDA -> "Tu solicitud llegó a CORPALIV."
        EN_REVISION -> "El equipo está revisando tu solicitud."
        CONTACTADO -> "El equipo ya se comunicó contigo."
        CONCRETADO -> "¡Gracias! La colaboración se concretó."
        DESCARTADO -> "Esta solicitud no pudo continuar."
        else -> ""
    }
}
