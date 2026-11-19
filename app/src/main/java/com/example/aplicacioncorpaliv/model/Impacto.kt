package com.example.aplicacioncorpaliv.model

// MODELOS de la pantalla Impacto. Son clases de datos simples: solo guardan información, no tienen lógica.
// EXPLICAR: los nombres de los campos siguen la Propuesta Técnica (historia_impacto e indicador_impacto).
// Los datos vienen de ms-contenidos (si no hay conexión, de DatosDeEjemplo). Estas mismas clases sirven para recibirlos.

// Una historia de impacto. Siempre debe estar autorizada y anonimizada (no se publican nombres ni fotos reales)
data class HistoriaImpacto(
    val id: Int,
    val titulo: String,
    val resumen: String,
    val contenido: String
)

// Un indicador de impacto: un número que muestra qué logra CORPALIV (por ejemplo, 40 personas en formación)
data class IndicadorImpacto(
    val id: Int,
    val nombre: String,
    val valor: Double,
    val unidad: String,
    val periodo: String
) {
    // EXPLICAR: el servidor entrega el valor como número con decimales (40.0). Para mostrar "40" y no "40.0",
    // si no tiene decimales se muestra como número entero.
    val valorTexto: String
        get() = if (valor % 1.0 == 0.0) valor.toLong().toString() else valor.toString()
}
