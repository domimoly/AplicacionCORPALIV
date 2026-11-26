package com.example.aplicacioncorpaliv.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aplicacioncorpaliv.model.TiposColaboracion

// COMPONENTE: tarjeta que explica cada modalidad de colaboración y a qué ayuda el aporte (la usa el formulario Colaborar).
// EXPLICAR: el caso pide mostrar "las modalidades de colaboración y el destino de los aportes". Los textos salen de
// TiposColaboracion.todos (campo 'descripcion'), así que si se agrega un tipo nuevo aparece aquí solo.
// 'abierto' guarda si la tarjeta está desplegada; al tocar el título cambia true/false y la pantalla se redibuja.
// rememberSaveable: si se gira el teléfono, la tarjeta sigue abierta o cerrada como estaba.
@Composable
fun ModalidadesColaboracion(modifier: Modifier = Modifier) {
    var abierto by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            // Parte siempre visible: el título y la flecha. Toda la fila es tocable
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { abierto = !abierto }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Cómo puedes colaborar y a qué ayuda tu aporte?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (abierto) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (abierto) "Ocultar modalidades" else "Ver modalidades"
                )
            }

            // Parte desplegable: una línea con el nombre y otra con la explicación de cada modalidad
            AnimatedVisibility(visible = abierto) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TiposColaboracion.todos.forEach { tipo ->
                        Column {
                            Text(
                                text = tipo.nombre,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = tipo.descripcion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    // Aclara que la app registra el interés; no cobra ni hace pagos (el MVP no incluye pagos en línea)
                    Text(
                        text = "Este formulario solo registra tu interés. El equipo de CORPALIV te contactará para concretar la colaboración; la app no realiza pagos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
