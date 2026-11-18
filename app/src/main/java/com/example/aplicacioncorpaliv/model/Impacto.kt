package com.example.aplicacioncorpaliv.model

// MODELOS de la pantalla Impacto. Son clases de datos simples: solo guardan información, no tienen lógica.
// EXPLICAR: los nombres de los campos siguen la Propuesta Técnica (historia_impacto e indicador_impacto).
// Hoy los datos vienen de ImpactoViewModel (de ejemplo); más adelante vendrán del microservicio ms-contenidos
// y estas mismas clases servirán para recibirlos.

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
    val valor: Int,
    val unidad: String,
    val periodo: String
)
