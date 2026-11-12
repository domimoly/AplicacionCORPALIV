package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.ui.componentes.ItemDetalle
import com.example.aplicacioncorpaliv.ui.componentes.OlaDecorativa
import com.example.aplicacioncorpaliv.ui.componentes.TarjetaExpandible
import com.example.aplicacioncorpaliv.ui.theme.AzulClaro
import com.example.aplicacioncorpaliv.ui.theme.AzulOscuro
import com.example.aplicacioncorpaliv.ui.theme.MoradoClaro
import com.example.aplicacioncorpaliv.ui.theme.MoradoOscuro
import com.example.aplicacioncorpaliv.ui.theme.RosaClaro
import com.example.aplicacioncorpaliv.ui.theme.RosaOscuro
import com.example.aplicacioncorpaliv.ui.theme.TerracotaClaro
import com.example.aplicacioncorpaliv.ui.theme.TerracotaOscuro
import com.example.aplicacioncorpaliv.ui.theme.VerdeAzuladoClaro
import com.example.aplicacioncorpaliv.ui.theme.VerdeAzuladoOscuro

// PANTALLA PROGRAMAS: cada programa es una tarjeta que se despliega al tocarla (componente TarjetaExpandible).
// EXPLICAR: la información está basada en lo que CORPALIV publica en su sitio web (corpaliv.cl).
// Para agregar un programa se copia un bloque TarjetaExpandible { ... } y se cambian los textos.
// onPantallaSeleccionada es la función de App.kt que cambia de pantalla (aquí lleva a 'Contacto').
@Composable
fun ProgramasScreen(
    modifier: Modifier = Modifier,
    onPantallaSeleccionada: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // La misma ola del Inicio, más baja, para que todas las pantallas se vean parte de la misma app
        OlaDecorativa(alto = 100.dp)

        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Nuestros programas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Así promovemos la autonomía, la participación y la inclusión social y laboral. Toca cada programa para ver más detalles.",
                style = MaterialTheme.typography.bodyMedium
            )

            TarjetaExpandible(
                icono = Icons.Filled.School,
                colorFondoIcono = MoradoClaro,
                colorIcono = MoradoOscuro,
                titulo = "Escuela Jan Van Dijk",
                resumen = "Educación para niñas, niños y jóvenes"
            ) {
                Text("Escuela especial gratuita para estudiantes con discapacidad múltiple y/o sordoceguera.")
                ItemDetalle("Niveles desde Medio Menor hasta 6° básico")
                ItemDetalle("Equipo de psicología, terapia ocupacional y fonoaudiología")
                ItemDetalle("Trabajo cercano con las familias")
                ItemDetalle("Autorizada por el Ministerio de Educación")
            }

            TarjetaExpandible(
                icono = Icons.Filled.Groups,
                colorFondoIcono = VerdeAzuladoClaro,
                colorIcono = VerdeAzuladoOscuro,
                titulo = "Taller Sociolaboral",
                resumen = "Alba Lab · formación para jóvenes y adultos"
            ) {
                Text("Espacio para explorar intereses y desarrollar habilidades para el trabajo.")
                ItemDetalle("Dirigido a jóvenes y adultos con discapacidad múltiple")
                ItemDetalle("Exploración y asesoramiento para la inclusión laboral")
                ItemDetalle("Se financia en parte con donaciones de empresas")
            }

            TarjetaExpandible(
                icono = Icons.Filled.Work,
                colorFondoIcono = AzulClaro,
                colorIcono = AzulOscuro,
                titulo = "Intermediación Laboral",
                resumen = "Alba Lab · del taller al primer empleo"
            ) {
                Text("Acompaña el paso al trabajo y conecta a las personas con empresas.")
                ItemDetalle("Análisis del puesto de trabajo")
                ItemDetalle("Acompañamiento durante la inserción laboral")
                ItemDetalle("Orientación para la empresa y su equipo")
            }

            TarjetaExpandible(
                icono = Icons.Filled.Business,
                colorFondoIcono = TerracotaClaro,
                colorIcono = TerracotaOscuro,
                titulo = "Servicios para empresas",
                resumen = "Capacitación, accesibilidad y más"
            ) {
                Text("Alba Lab ofrece a empresas y organizaciones:")
                ItemDetalle("Capacitaciones en inclusión y discapacidad")
                ItemDetalle("Experiencias de concientización")
                ItemDetalle("Análisis de puesto de trabajo")
                ItemDetalle("Políticas de inclusión laboral")
                ItemDetalle("Acompañamiento de inserción laboral")
                ItemDetalle("Evaluación de accesibilidad universal")
                ItemDetalle("Búsqueda inclusiva de personas (hunting inclusivo)")
            }

            TarjetaExpandible(
                icono = Icons.Filled.Info,
                colorFondoIcono = RosaClaro,
                colorIcono = RosaOscuro,
                titulo = "Ley de Inclusión Laboral",
                resumen = "Ley 21.015 y donaciones de empresas"
            ) {
                Text("Las empresas con 100 o más trabajadores deben contar con al menos un 1% de personas con discapacidad.")
                ItemDetalle("Si no pueden cumplirlo, existe la alternativa de donar a una fundación habilitada")
                ItemDetalle("CORPALIV puede recibir estas donaciones, que ayudan a financiar el Taller Sociolaboral")
                ItemDetalle("Para conocer los detalles, escríbenos desde Contacto")
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Botón final: lleva a la pantalla Contacto
            Button(
                onClick = { onPantallaSeleccionada("Contacto") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Quiero que me contacten")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
