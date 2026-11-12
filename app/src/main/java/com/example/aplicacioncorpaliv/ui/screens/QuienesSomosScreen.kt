package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.ui.componentes.BotonEscuchar
import com.example.aplicacioncorpaliv.ui.componentes.OlaDecorativa

// Una institución de CORPALIV (ícono, nombre y descripción corta)
private data class Institucion(
    val icono: ImageVector,
    val titulo: String,
    val descripcion: String
)

// Un hito de la historia: el año y qué pasó
private data class Hito(
    val anio: String,
    val texto: String
)

// Un indicador de impacto: ícono y texto
private data class Indicador(
    val icono: ImageVector,
    val texto: String
)

// PANTALLA QUIÉNES SOMOS: propósito, historia, instituciones, impacto e historias de ejemplo.
// EXPLICAR: la misión, la visión, los valores y la historia están basados en lo que CORPALIV publica en su sitio
// web (corpaliv.cl). Las cifras y las historias de impacto son FICTICIAS y anonimizadas (el caso no permite datos reales).
@Composable
fun QuienesSomosScreen(
    modifier: Modifier = Modifier
) {
    val instituciones = listOf(
        Institucion(
            Icons.Filled.School,
            "Escuela Especial Jan Van Dijk",
            "Educación gratuita para niñas, niños y jóvenes con discapacidad múltiple y/o sordoceguera."
        ),
        Institucion(
            Icons.Filled.Work,
            "Red Sociolaboral Alba Lab",
            "Formación, intermediación laboral y productos elaborados por jóvenes y adultos."
        )
    )

    val historia = listOf(
        Hito("1993", "Un grupo de padres que no encontraba opciones educativas para sus hijos crea la corporación."),
        Hito("2003", "Nace la Escuela Especial Jan Van Dijk."),
        Hito("Hoy", "Alba Lab acompaña a jóvenes y adultos en su camino hacia la inclusión laboral.")
    )

    // Datos de EJEMPLO (ficticios), los mismos del Inicio
    val indicadores = listOf(
        Indicador(Icons.Filled.Group, "40 personas en formación"),
        Indicador(Icons.Filled.Business, "8 empresas aliadas"),
        Indicador(Icons.Filled.Storefront, "250 productos elaborados")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        OlaDecorativa(alto = 100.dp)

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Quiénes somos",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "CORPALIV es una organización sin fines de lucro que promueve la autonomía, la participación y la inclusión social y laboral de personas con discapacidad múltiple.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            BotonEscuchar(
                texto = "Quiénes somos. CORPALIV es una organización sin fines de lucro que promueve la autonomía, la participación y la inclusión social y laboral de personas con discapacidad múltiple. Nuestra misión es acompañar el desarrollo de personas con discapacidad múltiple durante toda su vida. Nuestra visión es contribuir a un país sin barreras para la discapacidad múltiple."
            )

            // Misión, visión y valores: se reutiliza el componente TarjetaConIcono (más abajo)
            TarjetaConIcono(
                icono = Icons.Filled.Flag,
                titulo = "Misión",
                texto = "Acompañar el desarrollo de personas con discapacidad múltiple durante toda su vida, con un enfoque integral que respete sus necesidades y diferencias."
            )
            TarjetaConIcono(
                icono = Icons.Filled.Visibility,
                titulo = "Visión",
                texto = "Contribuir a un país sin barreras para la discapacidad múltiple."
            )
            TarjetaConIcono(
                icono = Icons.Filled.Favorite,
                titulo = "Valores",
                texto = "Compromiso, respeto, empatía y solidaridad."
            )

            Text(
                text = "Nuestra historia",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // forEach recorre la lista de hitos y dibuja una fila por cada uno: año a la izquierda, texto a la derecha
                    historia.forEach { hito ->
                        Row {
                            Text(
                                text = hito.anio,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.width(56.dp)
                            )
                            Text(
                                text = hito.texto,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            Text(
                text = "Nuestras instituciones",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            instituciones.forEach { institucion ->
                TarjetaConIcono(
                    icono = institucion.icono,
                    titulo = institucion.titulo,
                    texto = institucion.descripcion
                )
            }

            Text(
                text = "Nuestro impacto (datos de ejemplo)",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    indicadores.forEach { indicador ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = indicador.icono,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(indicador.texto)
                        }
                    }
                }
            }

            Text(
                text = "Historias de impacto (ejemplo)",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Historias ficticias y anonimizadas, solo para mostrar cómo se verían en la app.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TarjetaConIcono(
                icono = Icons.Filled.FormatQuote,
                titulo = "Una joven de 22 años",
                texto = "Participó en el Taller Sociolaboral y hoy realiza una práctica en una oficina, con el acompañamiento de un tutor."
            )
            TarjetaConIcono(
                icono = Icons.Filled.FormatQuote,
                titulo = "Un niño de 8 años",
                texto = "Con apoyo del equipo de la escuela y de su familia, hoy puede comunicar lo que quiere elegir cada día."
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Tarjeta con un ícono a la izquierda, un título y un texto. Se usa varias veces arriba para no repetir código
@Composable
private fun TarjetaConIcono(
    icono: ImageVector,
    titulo: String,
    texto: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = texto,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
