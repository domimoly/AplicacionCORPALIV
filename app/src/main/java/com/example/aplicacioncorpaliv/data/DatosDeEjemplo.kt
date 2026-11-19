package com.example.aplicacioncorpaliv.data

import com.example.aplicacioncorpaliv.model.HistoriaImpacto
import com.example.aplicacioncorpaliv.model.IndicadorImpacto
import com.example.aplicacioncorpaliv.model.ProductoItem
import com.example.aplicacioncorpaliv.model.Programa

// DATOS DE RESPALDO (todos de ejemplo, ficticios y anonimizados).
// EXPLICAR: si la app no logra conectarse al servidor, las pantallas muestran estos datos en vez de quedar vacías.
// Así la app sigue sirviendo aunque el backend esté apagado o no haya internet.
object DatosDeEjemplo {

    val programas = listOf(
        Programa(1, "Escuela Jan Van Dijk", "Educación para niñas, niños y jóvenes",
            "Escuela especial gratuita para estudiantes con discapacidad múltiple y/o sordoceguera. Niveles desde Medio Menor hasta 6° básico, con equipo de psicología, terapia ocupacional y fonoaudiología.",
            "Educación"),
        Programa(2, "Taller Sociolaboral", "Alba Lab · formación para jóvenes y adultos",
            "Espacio para explorar intereses y desarrollar habilidades para el trabajo. Dirigido a jóvenes y adultos con discapacidad múltiple.",
            "Formación laboral"),
        Programa(3, "Intermediación Laboral", "Alba Lab · del taller al primer empleo",
            "Acompaña el paso al trabajo y conecta a las personas con empresas. Incluye análisis del puesto de trabajo.",
            "Inclusión laboral"),
        Programa(4, "Servicios para empresas", "Capacitación, accesibilidad y más",
            "Capacitaciones en inclusión y discapacidad, experiencias de concientización, análisis de puesto de trabajo y evaluación de accesibilidad universal.",
            "Servicios a empresas"),
        Programa(5, "Ley de Inclusión Laboral", "Ley 21.015 y donaciones de empresas",
            "Las empresas con 100 o más trabajadores deben contar con al menos un 1% de personas con discapacidad. Si no pueden cumplirlo, existe la alternativa de donar a una fundación habilitada.",
            "Inclusión laboral")
    )

    val productos = listOf(
        ProductoItem("Difusor de aroma 100 ml", "Bienestar", "Ambienta tu espacio con aromas suaves, hecho a mano.", "\$7.500"),
        ProductoItem("Difusor de aroma 250 ml", "Bienestar", "Formato grande para hogares y oficinas.", "\$12.000"),
        ProductoItem("Difusor de varilla", "Bienestar", "Aroma continuo y decoración en un solo producto.", "\$17.000"),
        ProductoItem("Guatero de semillas grande", "Bienestar", "Calor natural para relajar, relleno con semillas.", "\$11.990"),
        ProductoItem("Farol de luz", "Otros", "Luz cálida para dar un toque especial a cualquier rincón.", "\$11.900"),
        ProductoItem("Bolsa Alba Lab", "Otros", "Bolsa reutilizable con el sello de Alba Lab.", "\$4.000"),
        ProductoItem("Capacitación en inclusión", "Servicios", "Charla o taller para equipos de empresas y organizaciones.", "A cotizar"),
        ProductoItem("Experiencia de concientización", "Servicios", "Actividad práctica para comprender la discapacidad múltiple.", "A cotizar"),
        ProductoItem("Evaluación de accesibilidad", "Servicios", "Revisión de espacios y procesos para que sean accesibles.", "A cotizar")
    )

    val indicadores = listOf(
        IndicadorImpacto(1, "Años de trayectoria", 32.0, "años", "2026"),
        IndicadorImpacto(2, "Personas en formación", 40.0, "personas", "2026"),
        IndicadorImpacto(3, "Empresas aliadas", 8.0, "empresas", "2026"),
        IndicadorImpacto(4, "Productos elaborados", 250.0, "unidades", "2026")
    )

    val historias = listOf(
        HistoriaImpacto(
            id = 1,
            titulo = "Un primer día de trabajo",
            resumen = "Un joven del Taller Sociolaboral",
            contenido = "Después de meses de práctica en el taller, un joven comenzó a trabajar medio día en una empresa aliada. " +
                    "Hoy realiza sus tareas con apoyo y su equipo celebra cada logro. Historia anonimizada y de ejemplo."
        ),
        HistoriaImpacto(
            id = 2,
            titulo = "Aprender jugando y comunicando",
            resumen = "Una niña de la Escuela Jan Van Dijk",
            contenido = "Con apoyos de comunicación y rutinas pensadas para ella, una niña aprendió a expresar lo que quiere y a " +
                    "compartir con sus compañeros. Su familia participa en cada etapa. Historia anonimizada y de ejemplo."
        ),
        HistoriaImpacto(
            id = 3,
            titulo = "Una alianza que abre puertas",
            resumen = "Una empresa que se sumó a la red",
            contenido = "Una empresa conoció el trabajo de CORPALIV, hizo un análisis de puestos junto al equipo y abrió una " +
                    "oportunidad laboral inclusiva. Historia anonimizada y de ejemplo."
        )
    )
}
