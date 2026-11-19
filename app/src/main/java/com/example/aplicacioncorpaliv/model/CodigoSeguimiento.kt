package com.example.aplicacioncorpaliv.model

// CÓDIGO DE SEGUIMIENTO de una solicitud, por ejemplo 'COL-7KQ4M2XA'.
// EXPLICAR: la persona no inicia sesión, así que este código es lo que le permite consultar el estado de SU solicitud
// (en 'Mis solicitudes'). Se arma con letras y números sin caracteres confusos (sin 0, O, 1, I).
// AVANZADO: ahora el código lo crea el SERVIDOR (ms-vinculacion) con este mismo formato y la app solo lo guarda.
object CodigoSeguimiento {
    private const val CARACTERES = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    private const val LARGO = 8
    private val FORMATO = Regex("^COL-[A-HJ-NP-Z2-9]{8}$")

    fun generar(): String {
        val aleatorio = (1..LARGO).map { CARACTERES.random() }.joinToString("")
        return "COL-$aleatorio"
    }

    // Quita espacios y pasa a mayúsculas, para que 'col-abcd2345 ' también funcione al escribirlo
    fun normalizar(texto: String): String = texto.trim().uppercase()

    fun esValido(texto: String): Boolean = FORMATO.matches(normalizar(texto))
}
