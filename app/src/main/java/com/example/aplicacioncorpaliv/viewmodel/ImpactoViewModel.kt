package com.example.aplicacioncorpaliv.viewmodel

import androidx.lifecycle.ViewModel
import com.example.aplicacioncorpaliv.model.HistoriaImpacto
import com.example.aplicacioncorpaliv.model.IndicadorImpacto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// EXPLICAR: este ViewModel entrega a la pantalla Impacto las historias y los indicadores.
// Por ahora son datos de EJEMPLO (ficticios y anonimizados, sin nombres ni fotos reales).
// Más adelante, en vez de escribirlos aquí, se pedirán al microservicio ms-contenidos con Retrofit,
// y la pantalla no tendría que cambiar porque sigue mirando los mismos StateFlow.
// ViewModel (y no AndroidViewModel) porque no necesita el contexto de la app: no guarda nada en el teléfono.
class ImpactoViewModel : ViewModel() {

    private val _indicadores = MutableStateFlow(
        listOf(
            IndicadorImpacto(1, "Años de trayectoria", 32, "años", "2026"),
            IndicadorImpacto(2, "Personas en formación", 40, "personas", "2026"),
            IndicadorImpacto(3, "Empresas aliadas", 8, "empresas", "2026"),
            IndicadorImpacto(4, "Productos elaborados", 250, "unidades", "2026")
        )
    )
    val indicadores: StateFlow<List<IndicadorImpacto>> = _indicadores.asStateFlow()

    private val _historias = MutableStateFlow(
        listOf(
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
    )
    val historias: StateFlow<List<HistoriaImpacto>> = _historias.asStateFlow()
}
