package com.example.aplicacioncorpaliv.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacioncorpaliv.ui.componentes.BotonEscuchar
import com.example.aplicacioncorpaliv.viewmodel.AccesibilidadViewModel

// PANTALLA DE ACCESIBILIDAD: permite cambiar el tamaño del texto y activar el alto contraste.
// EXPLICAR: la app está pensada para personas con distintas discapacidades (visual, motriz, cognitiva),
// por eso las opciones son simples y con botones grandes. Los cambios se ven al instante en TODA la app
// y quedan guardados en el teléfono (DataStore) aunque se cierre la app.
@Composable
fun AccesibilidadScreen(
    modifier: Modifier = Modifier,
    // viewModel() entrega el MISMO AccesibilidadViewModel que usa MainActivity para aplicar el tema
    accesibilidadViewModel: AccesibilidadViewModel = viewModel()
) {
    // collectAsState convierte el StateFlow del ViewModel en un estado de Compose: si cambia, la pantalla se redibuja
    val nivelTexto by accesibilidadViewModel.nivelTexto.collectAsState()
    val altoContraste by accesibilidadViewModel.altoContraste.collectAsState()
    val lecturaVoz by accesibilidadViewModel.lecturaVoz.collectAsState()

    // Las 3 opciones de tamaño. La posición en la lista (0, 1, 2) es lo que se guarda
    val opcionesTexto = listOf("Normal", "Grande", "Muy grande")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Comodidad de uso",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Ajusta la app a lo que te resulte más cómodo. Los cambios se aplican al instante y se guardan en tu teléfono.",
            style = MaterialTheme.typography.bodyMedium
        )

        // ---------- TAMAÑO DEL TEXTO ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Tamaño del texto",
                    style = MaterialTheme.typography.titleMedium
                )
                // selectableGroup avisa a lector de pantalla (TalkBack) que estas opciones son un grupo de las que se elige solo una
                Column(modifier = Modifier.selectableGroup()) {
                    // forEachIndexed recorre la lista y entrega la posición (indice) y el texto de cada opción
                    opcionesTexto.forEachIndexed { indice, texto ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                // selectable hace que TODA la fila sea tocable (no solo el circulito): botón más grande
                                .selectable(
                                    selected = nivelTexto == indice,
                                    onClick = { accesibilidadViewModel.cambiarNivelTexto(indice) },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = nivelTexto == indice,
                                onClick = null   // el clic lo maneja la fila completa
                            )
                            Text(
                                text = texto,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "Así se ve el texto con este tamaño.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ---------- ALTO CONTRASTE ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // weight(1f) hace que el texto ocupe el espacio sobrante y el switch quede a la derecha
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Alto contraste",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Fondo negro, letras blancas y detalles amarillos para distinguir mejor cada elemento.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = altoContraste,
                    onCheckedChange = { accesibilidadViewModel.cambiarAltoContraste(it) },
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }

        // ---------- LEER EN VOZ ALTA ----------
        // EXPLICAR: si está activado, aparece un botón 'Escuchar' en las pantallas. Al tocarlo, el teléfono lee el texto en voz alta
        // (ver LectorVoz.kt, donde está explicado cómo funciona). Sirve a quien no ve bien, a quien le cuesta leer o prefiere escuchar.
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Leer en voz alta",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Muestra un botón Escuchar en las pantallas para que el teléfono lea los textos.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = lecturaVoz,
                        onCheckedChange = { accesibilidadViewModel.cambiarLecturaVoz(it) },
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                // Botón para probar la voz (solo aparece si la opción está activada)
                BotonEscuchar(texto = "Esta es la voz de la aplicación CORPALIV. Así se escuchan los textos.")
            }
        }

        // ---------- RESTABLECER ----------
        OutlinedButton(
            onClick = { accesibilidadViewModel.restablecer() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restablecer opciones")
        }
    }
}
