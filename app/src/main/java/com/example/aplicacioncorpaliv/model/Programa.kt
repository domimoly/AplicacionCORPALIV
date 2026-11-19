package com.example.aplicacioncorpaliv.model

// Un programa de CORPALIV. Los nombres de los campos son los MISMOS que devuelve ms-contenidos en el JSON:
// así Gson (la librería que convierte JSON a Kotlin) los une solo.
// EXPLICAR: 'String?' significa que el valor puede venir vacío (null). imagenUrl casi siempre es null por ahora.
data class Programa(
    val id: Int,
    val nombre: String,
    val resumen: String,
    val descripcion: String,
    val categoria: String,
    val imagenUrl: String? = null
)
